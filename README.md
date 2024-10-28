# Proyecto UVG Manejo de Hospitales

## Creado por
- Adrian Arimany
-
-
-
-

## Configuracion
1. Tener instalado [java version 21](https://www.oracle.com/java/technologies/downloads/) (IMPORTANTE!!)

2. Clonar repositorio:

```bash
git clone https://github.com/adrianArimany/Manejo_Hospitales.git 
```

2. Instalar [Maven](https://maven.apache.org/install.html)

3. Instalar paquetes de maven

```bash
mvn clean install -DskipTests
```

4. correr programa

```bash
mvn exec:java
```

En caso que no funcione lo de arriba correrlo directo con:

```bash
mvn exec:java -Dexec.mainClass="com.uvg.proyecto.Main"
```