# CargoManagementSystem

![GUI](https://user-images.githubusercontent.com/65776440/100639274-404cf280-3335-11eb-8126-95c145c72f33.PNG)<br><br>
This project represents a scalable, offline Database-Management-System for both customers and their associated Cargo.<br>
While originally conceptualized as a <b>CommandLineInterface</b>, this project also provides a <b>GraphicalUserInterface</b>.<br><br>

![CLI](https://user-images.githubusercontent.com/65776440/100639340-565ab300-3335-11eb-907f-028da55cf145.PNG)<br><br>

Please find a list of some functionalities additional to the <b>CRUD</b>-mechanism the CargoManagementSystem provides:<br>

* Notification about stored content and storage capacity (using the <b>ObeserverPattern</b>)
* <b>Serialisation</b> of both data bases, using <b>JavaBeans</b>
* ProofOfConcept for increased scalability via <b>Threads</b> (see SimulationStarter.java)
<br>
To keep the individual parts of the project loosely connected <b>events</b> and <b>listener</b> are used for the interprocess communication.
