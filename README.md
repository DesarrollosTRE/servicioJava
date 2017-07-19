# servicioJava
Aplicación para escritorio en java encargada de rescatar información de una base de datos en SQL e insertar dicha información en MYSQL. Esto en Hilos separados y ejecutados con un intervalo de tiempo con ScheduledExecutorService. Los errores que se generen son delegados y posteriormente capturados para ser enviados por email y mostrar en Log de aplicación.

# Form Principal
![vistaprincipal](https://user-images.githubusercontent.com/22084653/28380706-51fbd934-6c86-11e7-93d4-6a523528f194.png)

# Clase encargada de correr Hilos 
![codigoservicio](https://user-images.githubusercontent.com/22084653/28380707-51fd4a08-6c86-11e7-8ff1-c84dbceee8ce.png)

# Interface con CRUD
![crudmysql](https://user-images.githubusercontent.com/22084653/28380708-51ff0d16-6c86-11e7-9993-f926e0093bca.png)

# Throwing de Errores desde lista de Objetos
Interface Funcional encargada de lanzar errores generados al insertar objetos desde lista.

![trhowinglambda](https://user-images.githubusercontent.com/22084653/28380705-51fb8a74-6c86-11e7-98a4-34c0ba58476d.png)
