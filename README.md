# Maven Plugin for Blue Cave

[![Code Quality](https://cloud.bluecave.io/badges/gh/bluecave-toolbox/bluecave-maven-plugin/analysis.svg)](https://cloud.bluecave.io/projects/gh/bluecave-toolbox/bluecave-maven-plugin)
[![Coverage](https://cloud.bluecave.io/badges/gh/bluecave-toolbox/bluecave-maven-plugin/coverage.svg)](https://cloud.bluecave.io/projects/gh/bluecave-toolbox/bluecave-maven-plugin)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A Maven plugin that provides a wrapper for Blue Cave's CLI, making it easier to integrate Blue Cave
into your project.

## Usage
Add the plugin to your `pom.xml` file:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.bluecave</groupId>
            <artifactId>bluecave-maven-plugin</artifactId>
            <version>0.1.1</version>
        </plugin>
    </plugins>
</build>
```

You can now use `bluecave:report` after you run your tests to analyze and report coverage to your Blue Cave project:
```shell
export BLUECAVE_TOKEN="<your project token>" # Please keep this a secret!
# The following is only required if running outside of GitHub Actions:
# export BLUECAVE_EXTRA_OPTS="-b <branch name, such as main> -c <commit hash to attribute this analysis to>"
# See https://docs.bluecave.io/ci/ for more information. 
./mvnw test
./mvnw bluecave:report
```

If you run into any trouble, please open an issue :)

## Contributing
Pull requests are welcome! For major changes, please open an issue to discuss your change first.

## License
[MIT](https://github.com/bluecave-toolbox/bluecave-maven-plugin/blob/main/LICENSE).
