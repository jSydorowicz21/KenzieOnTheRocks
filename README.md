<p align="center">
  <img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg" width="100" alt="project-logo">
</p>
<p align="center">
    <h1 align="center">Kenzie On the Rocks</h1>
</p>
<p align="center">
    <em><code>► INSERT-TEXT-HERE</code></em>
</p>
<p align="center">
	<!-- local repository, no metadata badges. -->
<p>
<p align="center">
		<em>Developed with the software and tools below.</em>
</p>
<p align="center">
	<img src="https://img.shields.io/badge/GNU%20Bash-4EAA25.svg?style=default&logo=GNU-Bash&logoColor=white" alt="GNU%20Bash">
	<img src="https://img.shields.io/badge/JavaScript-F7DF1E.svg?style=default&logo=JavaScript&logoColor=black" alt="JavaScript">
	<img src="https://img.shields.io/badge/HTML5-E34F26.svg?style=default&logo=HTML5&logoColor=white" alt="HTML5">
	<img src="https://img.shields.io/badge/PostCSS-DD3A0A.svg?style=default&logo=PostCSS&logoColor=white" alt="PostCSS">
	<img src="https://img.shields.io/badge/Autoprefixer-DD3735.svg?style=default&logo=Autoprefixer&logoColor=white" alt="Autoprefixer">
	<img src="https://img.shields.io/badge/Apache-D22128.svg?style=default&logo=Apache&logoColor=white" alt="Apache">
	<img src="https://img.shields.io/badge/YAML-CB171E.svg?style=default&logo=YAML&logoColor=white" alt="YAML">
	<img src="https://img.shields.io/badge/Webpack-8DD6F9.svg?style=default&logo=Webpack&logoColor=black" alt="Webpack">
	<img src="https://img.shields.io/badge/Bootstrap-7952B3.svg?style=default&logo=Bootstrap&logoColor=white" alt="Bootstrap">
	<img src="https://img.shields.io/badge/Org-77AA99.svg?style=default&logo=Org&logoColor=white" alt="Org">
	<img src="https://img.shields.io/badge/React-61DAFB.svg?style=default&logo=React&logoColor=black" alt="React">
	<br>
	<img src="https://img.shields.io/badge/Axios-5A29E4.svg?style=default&logo=Axios&logoColor=white" alt="Axios">
	<img src="https://img.shields.io/badge/Google-4285F4.svg?style=default&logo=Google&logoColor=white" alt="Google">
	<img src="https://img.shields.io/badge/SemVer-3F4551.svg?style=default&logo=SemVer&logoColor=white" alt="SemVer">
	<img src="https://img.shields.io/badge/Lodash-3492FF.svg?style=default&logo=Lodash&logoColor=white" alt="Lodash">
	<img src="https://img.shields.io/badge/Ajv-23C8D2.svg?style=default&logo=Ajv&logoColor=white" alt="Ajv">
	<img src="https://img.shields.io/badge/GitHub-181717.svg?style=default&logo=GitHub&logoColor=white" alt="GitHub">
	<img src="https://img.shields.io/badge/Gradle-02303A.svg?style=default&logo=Gradle&logoColor=white" alt="Gradle">
	<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=default&logo=openjdk&logoColor=white" alt="java">
	<img src="https://img.shields.io/badge/Express-000000.svg?style=default&logo=Express&logoColor=white" alt="Express">
	<img src="https://img.shields.io/badge/JSON-000000.svg?style=default&logo=JSON&logoColor=white" alt="JSON">
</p>

<br><!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary><br>

- [ Overview](#-overview)
- [ Features](#-features)
- [ Repository Structure](#-repository-structure)
- [ Getting Started](#-getting-started)
  - [ Installation](#-installation)
  - [ Usage](#-usage)
  - [ Tests](#-tests)
- [ Project Roadmap](#-project-roadmap)
- [ Contributing](#-contributing)
- [ License](#-license)
- [ Acknowledgments](#-acknowledgments)
</details>
<hr>

##  Overview

KenzieOnTheRocks is a Java/AWS drink recipe sharing platform, originally developed as a capstone project for the Kenzie Academy ATA Program. This project is now maintained to keep skills sharp and provide a learning resource for new developers.

## Features

- **User Authentication:** AWS Cognito for secure user login and registration.
- **Recipe Management:** CRUD operations for drink recipes using AWS DynamoDB.
- **Image Storage:** AWS S3 integration for storing recipe images.
- **Serverless Architecture:** AWS Lambda functions for backend logic.
- **CI/CD Pipeline:** Automated deployment using custom scripts.

---

##  Repository Structure

```sh
└── \Users\sydor\Kenzie\KenzieOnTheRocks/
    ├── .github
    │   ├── .keep
    │   └── workflows
    ├── Application
    │   ├── build
    │   ├── build.gradle
    │   └── src
    ├── Application-template.yml
    ├── build.gradle
    ├── buildScripts
    │   ├── buildspec.yml
    │   ├── CICDPipeline-Capstone.yml
    │   ├── integrationspec.yml
    │   └── runIntegrationTests.sh
    ├── buildSrc
    │   ├── build.gradle
    │   └── src
    ├── cleanupDev.sh
    ├── cleanupPipeline.sh
    ├── createPipeline.sh
    ├── deployDev.sh
    ├── fix-yarn-build-unit-4.patch
    ├── Frontend
    │   ├── build.gradle
    │   ├── package-lock.json
    │   ├── package.json
    │   ├── src
    │   ├── webpack.config.js
    │   └── yarn.lock
    ├── gradle
    │   └── wrapper
    ├── gradlew
    ├── gradlew.bat
    ├── IntegrationTests
    │   ├── build.gradle
    │   └── src
    ├── lambda-service-development.yml
    ├── LambdaExampleTable.yml
    ├── LambdaService-template.yml
    ├── local-dynamodb.sh
    ├── README.md
    ├── runLocalRedis.sh
    ├── ServiceLambda
    │   ├── build.gradle
    │   └── src
    ├── ServiceLambdaJavaClient
    │   ├── build.gradle
    │   └── src
    ├── ServiceLambdaModel
    │   ├── build.gradle
    │   └── src
    ├── settings.gradle
    ├── setupEnvironment.sh
    ├── UserTable.yaml
    ├── Utilities
    │   ├── ATACheckstyle
    │   ├── bin
    │   ├── build.gradle
    │   └── src
    └── yarn.lock
```

---

##  Getting Started

**System Requirements:**

* **Java**: `version x.y.z`

###  Installation

<h4>From <code>source</code></h4>

> 1. Clone the KenzieOnTheRocks repository:
>
> ```console
> $ git clone https://github.com/jSydorowicz21/KenzieOnTheRocks.git
> ```
>
> 2. Change to the project directory:
> ```console
> $ cd ./KenzieOnTheRocks
> ```
>
> 3. Install the dependencies:
> ```console
> $ mvn clean install
> ```

###  Usage

<h4>From <code>source</code></h4>

# Overview

### To deploy the Development Environment

Run `./deployDev.sh`

To tear down the deployment then run `./cleanupDev.sh`

### To deploy the CI/CD Pipeline

Fill out `setupEnvironment.sh` with the url of the GitHub repo and the username (in all lowercase) of the 
team member who is maintaining the repo. Confirm that the team member has added your username as a contributor to the repo.

Run `./createPipeline.sh`

To teardown the pipeline, run `./cleanupPipeline.sh`

---

## Current Backlog

https://github.com/jSydorowicz21/KenzieOnTheRocks/issues
---

##  Contributing

Contributions are welcome! Here are several ways you can contribute:

- **[Report Issues](https://github.com/jSydorowicz21/KenzieOnTheRocks/issues)**: Submit bugs found or log feature requests for the `\Users\sydor\Kenzie\KenzieOnTheRocks` project.
- **[Submit Pull Requests](https://github.com/jSydorowicz21/KenzieOnTheRocks/pulls)**: Review open PRs, and submit your own PRs.
- **[Join the Discussions](https://github.com/jSydorowicz21/KenzieOnTheRocks/discussions)**: Share your insights, provide feedback, or ask questions.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your local account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/jSydorowicz21/KenzieOnTheRocks.git
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to local**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>

<details closed>
<summary>Contributor Graph</summary>
<br>
<p align="center">
   <a href="https://github.com/jSydorowicz21/KenzieOnTheRocks/graphs/contributors">
      <img src="https://contrib.rocks/image?repo=KenzieOnTheRocks">
   </a>
</p>
</details>

---

##  License

This project is protected under the [SELECT-A-LICENSE](https://choosealicense.com/licenses) License. For more details, refer to the [LICENSE](https://choosealicense.com/licenses/) file.

---

##  Acknowledgments

- TODO: Fill out

[**Return**](#-overview)

---
