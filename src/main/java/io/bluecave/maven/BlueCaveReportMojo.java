package io.bluecave.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mojo(name = "report", defaultPhase = LifecyclePhase.NONE, aggregator = true, threadSafe = true)
public class BlueCaveReportMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            final var modules = Optional.ofNullable(project.getCollectedProjects())
                    .filter(list -> !list.isEmpty()).orElse(List.of(project));

            final var sources = new ArrayList<String>();
            final var classes = new ArrayList<String>();

            final var basePath = project.getBasedir().getAbsolutePath() + File.separator;

            for (MavenProject module : modules) {
                for (String source : module.getCompileSourceRoots()) {
                    sources.add(source.replace(basePath, ""));
                }

                for (String source : module.getTestCompileSourceRoots()) {
                    sources.add(source.replace(basePath, ""));
                }

                classes.add(module.getBuild().getOutputDirectory().replace(basePath, ""));
            }

            sources.removeIf(s -> !new File(basePath + s).isDirectory());
            classes.removeIf(s -> !new File(basePath + s).isDirectory());

            var command = "./bluecave -l java"
                    + " -s " + String.join(" -s ", sources)
                    + " -x " + String.join(" -x ", classes);

            if (System.getenv("BLUECAVE_EXTRA_OPTS") != null) {
                command += " " + System.getenv("BLUECAVE_EXTRA_OPTS");
            }

            runCommand("curl -sL https://get.bluecave.io | bash");
            runCommand(command);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to run Blue Cave report", e);
        }
    }

    private void runCommand(String command) throws IOException, InterruptedException {
        getLog().info("Running: " + command);
        var process = new ProcessBuilder("bash", "-c", command)
                .redirectErrorStream(true)
                .start();

        try (var in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                getLog().info(line);
            }
        }

        var exit = process.waitFor();
        if (exit != 0) {
            throw new IOException("Command failed with exit code " + exit);
        }
    }
}
