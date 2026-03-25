@echo off

REM === Mensaje bienvenida
echo Se va a compilar la practica 2025/2026 con las dependencias y restricciones


REM === Inputs ===
set SRC_DIR=%cd%\src
set BIN_DIR=\bin
set MAIN=es/uned/lsi/eped/pract2025_2026/Main
set JAVA_HOME_JDK="C:\Program Files\Java\jdk-26"
set TMP_FOLDER=%cd%\juego_de_pruebas_2026\tmp

IF %JAVA_HOME_JDK%=="" (

	IF "%JAVA_HOME%" == "" (
	    echo Modifica la variable del archivo bat JAVA_HOME_JDK 
	    pause
	    exit
	) ELSE (
	    set JAVA_HOME_JDK="%JAVA_HOME%"
	)
)


REM === Mostramos variables ===
echo Carpeta codigo = %SRC_DIR%
echo Carpeta bin = %BIN_DIR%
echo Clase principal = %MAIN%
echo JAVA_HOME_JDK = %JAVA_HOME_JDK%
echo. 
echo.


REM === Clean and make temp dir ===
echo Limpiando compilacion anterior 
rd /q /s "%TMP_FOLDER%"
pause
if not exist "%TMP_FOLDER%" mkdir "%TMP_FOLDER%" 
mkdir "%TMP_FOLDER%%BIN_DIR%"
mkdir "%TMP_FOLDER%\src"
mkdir "%TMP_FOLDER%\src\es"
mkdir "%TMP_FOLDER%\src\es\uned"
mkdir "%TMP_FOLDER%\src\es\uned\lsi"
mkdir "%TMP_FOLDER%\src\es\uned\lsi\eped"
mkdir "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2025_2026"
xcopy /s/q "%SRC_DIR%\es\uned\lsi\eped\pract2025_2026" "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2025_2026"
xcopy /s/y/q "juego_de_pruebas_2026\lib\src" "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2025_2026"

echo.
echo.
pause


REM ===

REM ===========================================
REM === Comprobacion uso TAD equipo docente ===
REM ===========================================
echo Comprobando el uso de estructuras de datos del equipo docente
cd "%TMP_FOLDER%\src\es\uned\lsi\eped\pract2025_2026"
find "import" IndexSequence.java IndexTree.java | find /v "es.uned.lsi.eped.DataStructures"
cd "../../../../../../../../"
echo Si se muestra algun "import" en el mensaje anterior es posible que no se este haciendo uso de las estructuras de datos del equipo docente
echo. 
echo.
pause


REM ===

REM ===============
REM === Compile ===
REM ===============
echo Compilando en carpeta temporal

%JAVA_HOME_JDK%"\bin\javac" -d "%TMP_FOLDER%%BIN_DIR%" -sourcepath "%TMP_FOLDER%\src" -cp "juego_de_pruebas_2026/lib/TAD_modified.jar" "%TMP_FOLDER%\src\%MAIN%.java"
if errorlevel 1 (
	echo Compilacion con errores
	pause
	exit /B 1
)

echo Compilacion sin errores
echo.
echo.
pause


REM =========================================
REM === Run Prueba Estudiantes 1 SEQUENCE ===
REM =========================================
echo Ejecutando el programa con prueba 1 SEQUENCE

%JAVA_HOME_JDK%"\bin\java" -cp "%TMP_FOLDER%%BIN_DIR%;juego_de_pruebas_2026/lib/TAD_modified.jar" "%MAIN%" SEQUENCE "juego_de_pruebas_2026/pruebas/JuegoPruebas_EST_1.txt" "juego_de_pruebas_2026/salida/Salida_EST_SEQ_1.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)

echo Ejecucion sin errores
echo. 
echo.
pause


REM === Comprobacion Estudiantes 1 SEQUENCE ===
echo Comprobando bateria de pruebas para prueba 1 SEQUENCE

%JAVA_HOME_JDK%"\bin\java" -jar "juego_de_pruebas_2026/lib/Comparator.jar" "juego_de_pruebas_2026/salida/Salida_EST_SEQ_1.txt" "juego_de_pruebas_2026/salida/Salida_1.txt" "errores_1.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)
echo. 
echo.
pause


REM ==============================================================================



REM =====================================
REM === Run Prueba Estudiantes 1 TREE ===
REM =====================================
echo Ejecutando el programa con prueba basica TREE

%JAVA_HOME_JDK%"\bin\java" -cp "%TMP_FOLDER%%BIN_DIR%;juego_de_pruebas_2026/lib/TAD_modified.jar" "%MAIN%" TREE "juego_de_pruebas_2026/pruebas/JuegoPruebas_EST_1.txt" "juego_de_pruebas_2026/salida/Salida_EST_TREE_1.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)

echo Ejecucion sin errores
echo. 
echo.
pause


REM === Comprobacion Estudiantes 1 TREE ===
echo Comprobando bateria de pruebas para prueba basica TREE

%JAVA_HOME_JDK%"\bin\java" -jar "juego_de_pruebas_2026/lib/Comparator.jar" "juego_de_pruebas_2026/salida/Salida_EST_TREE_1.txt" "juego_de_pruebas_2026/salida/Salida_1.txt" "errores_2.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)
echo. 
echo.
pause


REM ==============================================================================


REM ========================================
REM === Run Prueba Estudiantes 2 SEQUENCE===
REM ========================================
echo Ejecutando el programa con prueba 2 SEQUENCE

%JAVA_HOME_JDK%"\bin\java" -cp "%TMP_FOLDER%%BIN_DIR%;juego_de_pruebas_2026/lib/TAD_modified.jar" "%MAIN%" SEQUENCE "juego_de_pruebas_2026/pruebas/JuegoPruebas_EST_2.txt" "juego_de_pruebas_2026/salida/Salida_EST_SEQ_2.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)

echo Ejecucion sin errores
echo. 
echo.
pause


REM === Comprobacion Estudiantes 2 SEQUENCE ===
echo Comprobando bateria de pruebas para prueba 2 SEQUENCE

%JAVA_HOME_JDK%"\bin\java" -jar "juego_de_pruebas_2026/lib/Comparator.jar" "juego_de_pruebas_2026/salida/Salida_EST_SEQ_2.txt" "juego_de_pruebas_2026/salida/Salida_2.txt" "errores_3.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)
echo. 
echo.
pause


REM ==============================================================================



REM =====================================
REM === Run Prueba Estudiantes 2 TREE ===
REM =====================================
echo Ejecutando el programa con prueba 2 TREE

%JAVA_HOME_JDK%"\bin\java" -cp "%TMP_FOLDER%%BIN_DIR%;juego_de_pruebas_2026/lib/TAD_modified.jar" "%MAIN%" TREE "juego_de_pruebas_2026/pruebas/JuegoPruebas_EST_2.txt" "juego_de_pruebas_2026/salida/Salida_EST_TREE_2.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)

echo Ejecucion sin errores
echo. 
echo.
pause


REM === Comprobacion Estudiantes 2 TREE ===
echo Comprobando bateria de pruebas para prueba 2 TREE

%JAVA_HOME_JDK%"\bin\java" -jar "juego_de_pruebas_2026/lib/Comparator.jar" "juego_de_pruebas_2026/salida/Salida_EST_TREE_2.txt" "juego_de_pruebas_2026/salida/Salida_2.txt" "errores_4.txt"

if errorlevel 1 (
	echo Ejecucion con errores
	pause
	exit /B 1
)
echo. 
echo.
pause


REM ==============================================================================
