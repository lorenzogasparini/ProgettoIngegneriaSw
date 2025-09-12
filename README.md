<a id="readme-top"></a>
[![Contributors][contributors-shield]][contributors-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Unlicense License][license-shield]][license-url]
![java-shield]

<!-- [Funzioni da implementare](https://gist.github.com/AndreaPellizzari/7df0bee0c5017cf846bf2cb3f5d8702c) -->
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <h1 align="center">Progetto ingegneria del software</h1>

  <p align="center">
    Elaborato conclusivo del corso di Ingegneria del Software: Servizio clinico per la gestione di pazienti diabetici.
    <br />
    <i>Final project for the a.y. 2024-2025 Software Engineering course: Clinical service for the management of diabetic patients.</i>
    <br />
    <br />
    <a href="https://github.com/lorenzogasparini/ProgettoIngegneriaSw/tree/main/doc"><strong>Explore the docs »</strong></a>
    <br />
    <a href="https://github.com/lorenzogasparini/ProgettoIngegneriaSw/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    &middot;
    <a href="https://github.com/lorenzogasparini/ProgettoIngegneriaSw/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>
-->

<!-- ABOUT THE PROJECT -->
## About The Project

The project aims to supply a diabetes patients managing software platform (type 2 diabetes mellitus). 

Diabetes is a chronic metabolic disease characterised by high blood glucose levels. The users of the system are therefore **patients** and **doctors** - alias diabetologists - (**diabetologists** belonging to a common medical institute) who treat them.

The system will allow doctors and patients to interact with regard to the details involved in managing the disease itself. The system will be coordinated by users identified as responsible for the telemedicine service, who will enter and subsequently manage the initial data of patients and doctors necessary for authentication, referred to below as **admins**.

Each doctor will be able to analyse and update the data of each patient authenticated in the system at any time (this data includes the patient's personal details and a brief medical history; furthermore, these changes can be analysed and managed by the service manager (admin). Any user who needs to interact with the system must be able to authenticate themselves. The actual creation of the system is the direct responsibility of the administrator users.

The core functionality on which the system is based, as mentioned above, is the **management of details relating to the treatment** of the patient's conditions, linked to their history of type 2 diabetes: In fact, one of the basic functions provided by the system is the **management of blood glucose level measurements**, which may need to be taken several times a day. The system also allows the user to record other health-related data, such as weight, blood pressure, and heart rate, as well as to monitor the results of previous measurements. Once authenticated, the diabetologist will be able to **specify and manage the therapies prescribed** for patients in the system (either those under his care or general patients under the care of other doctors), where management means supervising the correct intake of medication and any symptoms experienced by the patient at any given time, who will clearly be able to interact with the system to view and enter this information. The system will have to **verify the safety and correctness of blood glucose levels** and compliance with the therapies assigned to it by an internal doctor, and **send a notification** in the event of situations that are not in line with the correct ones.


<div>
  <p>
    The product of the specification, design and documentation procedure, defined entirely in UML notation, is contained in its entirety in the document written in Italian and includes: <b>Use cases and UML specification</b>, <b>Sequence and interaction diagrams</b>, <b>Activity diagrams</b>, <b>Class diagrams</b>, as well as documentation of the <b>Software system design and development process</b> (containing: documentation relating to the application architecture, <b>architectural design</b>, <b>database design</b> (E/R schema, restructuring into SQL tables), <b>architectural patterns</b> and <b>design patterns</b>, as well as a description of some implementation details and the <b>process</b> (model and its implementation in the programming phase) <b>of software development</b>) and <b>Testing phase</b> (including: documentation of the unit testing operations used, as well as the test scenarios considered and the system release test).
    <br />
    <br />
    <i>Il prodotto della procedura di specifica, progettazione e documentazione, definita completamente in notazione UML è integralmente contenuto nel documento redatto in lingua italiana e comprende: <b>Casi d'uso e specifica UML</b>, <b>Diagrammi di sequenza ed interazione</b>, <b>Diagrammi di attività</b>, <b>Diagramma delle classi</b>, nonchè documentazione del <b>Processo di progettazione e sviluppo del sistema software</b> (contenente: la documentazione relativa all'architettura dell'applicazione, la <b>progettazione architetturale</b>, la <b>progettazione della base di dati</b> (Schema E/R, ristrutturazione in tabelle SQL), <b>pattern architetturali</b> e <b>design pattern</b> nonchè la descrizione di alcuni dettagli implementativi e del <b>processo</b> (modello e sua attuazione in fase di programmazione) <b>di sviluppo software</b>) e <b>Fase di test</b> (comprendente: documentazione delle operazioni di Unit testing utilizzate, nonchè gli scenari di test considerati e il test di rilascio del sistema).</i>
  </p>
</div>

Please refer to the `doc/Documentazione.pdf` file for further information regarding documentation written in UML formalism.

_Per ulteriori informazioni sulla documentazione redatta in formalismo UML, consultare il file `doc/Documentazione.pdf`._

### Built With
The main libraries and the database system used for the development of the project are listed below.

_Di seguito si indicano le principali librerie, nonchè il sistema di database utilizzato per lo sviluppo del progetto._

* ![JavaFX](https://img.shields.io/badge/JavaFX-17.0.6-green?style=for-the-badge)
* ![SQLite][sqlite-shield]

<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Installation

Below are the instructions for installing the software system.

_Di seguito sono indicate le istruzioni per l'installazione del sistema software._

1. Set up the prerequisites listed in the 'Build With' section.
2. Clone the repo:
   ```sh
   git clone https://github.com/lorenzogasparini/ProgettoIngegneriaSw
   ```
3. Change the remote URL of git to avoid accidental pushes to the base project:
   ```sh
   git remote set-url origin lorenzogasparini/ProgettoIngegneriaSw
   git remote -v # confirm the changes
   ```
4. Setup of module settings based on prerequisites.

<!-- USAGE EXAMPLES -->
## Usage

**INSERIRE ISTRUZIONI SULL'UTILIZZO E DOCUMENTAZIONE + IMMAGINI SISTEMA SOFTWARE IN UTILIZZO**

Use this space to show useful examples of how a project can be used. Additional screenshots, code examples and demos work well in this space. You may also link to more resources. For further information on using or contributing the software system, please refer to [Documentation](https://github.com/lorenzogasparini/ProgettoIngegneriaSw/blob/main/doc/Documentazione.pdf) section.

_Per ulteriori informazioni rispetto l'utilizzo del sistema software consultare [Documentazione](https://github.com/lorenzogasparini/ProgettoIngegneriaSw/blob/main/doc/Documentazione.pdf)_.

<!-- ROADMAP -->
## Roadmap

- [x] Design and documentation of the design phase
- [X] Development phase
- [X] Development phase documentation
- [X] Testing
- [X] Testing phase documentation
- [X] Release

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Top contributors:

<a href="https://github.com/lorenzogasparini/ProgettoIngegneriaSw/graphs/contributors">
  <!-- <img src="https://contrib.rocks/image?repo=lorenzogasparini/ProgettoIngegneriaSw" alt="contrib.rocks image" /> -->
  <img src="https://contrib.rocks/image?repo=lorenzogasparini/ProgettoIngegneriaSw" />
</a>

<!-- LICENSE -->
## License

Distributed under MIT License. See `LICENSE.txt` for more information.

_Distribuito sotto licenza MIT. Visualizza `LICENSE.txt` per ulteriori informazioni._
<!-- CONTACT -->
## Contact

Lorenzo Gasparini - lorenzo.gasparini.keke@gmail.com
<br />
Andrea Pellizzari - andrea.pellizzari19024@gmail.com

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/lorenzogasparini/ProgettoIngegneriaSw.svg?style=for-the-badge
[contributors-url]: https://github.com/lorenzogasparini/ProgettoIngegneriaSw/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/lorenzogasparini/ProgettoIngegneriaSw.svg?style=for-the-badge
[forks-url]: [https://github.com/lorenzogasparini/ProgettoIngegneriaSw](https://github.com/lorenzogasparini/ProgettoIngegneriaSw)/network/members

[stars-shield]: https://img.shields.io/github/stars/lorenzogasparini/ProgettoIngegneriaSw.svg?style=for-the-badge
[stars-url]: https://github.com/lorenzogasparini/ProgettoIngegneriaSw/stargazers

[issues-shield]: https://img.shields.io/github/issues/lorenzogasparini/ProgettoIngegneriaSw.svg?style=for-the-badge
[issues-url]: https://github.com/lorenzogasparini/ProgettoIngegneriaSw/issues

[license-shield]: https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge
[license-url]: https://github.com/lorenzogasparini/ProgettoIngegneriaSw/blob/main/LICENSE

[product-screenshot]: images/screenshot.png

[java-shield]: https://img.shields.io/badge/Java-23-red?style=for-the-badge
[sqlite-shield]: https://img.shields.io/badge/Database-SQLite-blue?style=for-the-badge
