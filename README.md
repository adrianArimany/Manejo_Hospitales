# UVG PROYECT - Hospital Management System

## Contributors:
- Adrian Arimany 


## Configurations:
1. Have version: [java version 21](https://www.oracle.com/java/technologies/downloads/) (Important!!)

2. Clone repository:

```bash
git clone https://github.com/adrianArimany/Manejo_Hospitales.git 
```

2. Install [Maven](https://maven.apache.org/install.html)

3. Install any package from maven:

```bash
mvn clean install -DskipTests
```

4. To execute the program:

```bash
mvn exec:java
```

In case the above doesn't run, try:

```bash
mvn exec:java -Dexec.mainClass="com.uvg.proyecto.Main"
```

### Program Explanation:

This program is inteded to be used primarly for hospitals, but have the following in mind:
1. The program is intended for staff only from the hospital (i.e. patients aren't to use the program at any point).
2. The program has three main classes: Paciente, Doctor, and Clinica. Each with a corresponding json file.
3. The json files are handled in storageHandler.

4. Some parts of the program are managed by an administrator, which there is a username and password to access the admin menu. 
* The current username: admin
* The current password: admin

These can be changed in system; 

(to changed them manually go to /src/main/java/com/uvg/proyecto/Properties/hospitalData.properties)

### Extra Info:

The Design Analysis and UML and other diagrams can be found in /root/diagrams/..
