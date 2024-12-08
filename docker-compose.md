Archivo docker-compose.yml:

version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres_container
    restart: always
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: mydb
    ports:
      - "5432:5432" # Puerto externo:interno
    volumes:
      - postgres_data:/var/lib/postgresql/data # Persistencia de datos

  pgadmin:
    image: dpage/pgadmin4
    container_name: pgadmin_container
    restart: always
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@example.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "8080:80" # Acceso a PgAdmin en el navegador
    depends_on:
      - postgres # Asegura que PostgreSQL esté corriendo antes de iniciar PgAdmin

volumes:
  postgres_data:
  
  
Cómo ejecutar este archivo
Crea el archivo docker-compose.yml: Guarda el contenido anterior en el directorio de tu proyecto o en un directorio dedicado para tus contenedores de base de datos.

Levanta los contenedores: Ejecuta el siguiente comando desde el directorio donde está tu archivo docker-compose.yml:

docker-compose up -d

Esto descargará las imágenes necesarias (si aún no las tienes) y levantará los contenedores en segundo plano.

Verifica que los contenedores estén corriendo: Usa el comando:

docker ps

Deberías ver algo como:

CONTAINER ID   IMAGE             STATUS         PORTS
123abc456def   postgres:15       Up 10 seconds  0.0.0.0:5432->5432/tcp
789ghi012jkl   dpage/pgadmin4    Up 10 seconds  0.0.0.0:8080->80/tcp

Accede a PgAdmin4: Abre tu navegador y visita http://localhost:8080. Usa el correo admin@example.com y contraseña admin para iniciar sesión.

Conecta tu aplicación de Spring Boot: La configuración en tu archivo application.properties es correcta. Tu aplicación ya debería conectarse automáticamente al contenedor de PostgreSQL porque estás usando localhost y el puerto 5432.

Cómo verificar la conexión en PgAdmin
Una vez dentro de PgAdmin, agrega un nuevo servidor:

Name: PostgreSQL (o cualquier nombre que prefieras).
Host: postgres (el nombre del servicio en el archivo docker-compose.yml).
Port: 5432.
Username: admin.
Password: admin.

Después de configurar, deberías poder explorar tu base de datos desde la interfaz de PgAdmin.

Notas adicionales

Persistencia: Los datos se guardarán en el volumen postgres_data. Si borras los contenedores, los datos no se perderán a menos que también elimines el volumen.

Reiniciar contenedores: Puedes detener y reiniciar los contenedores con los comandos:
docker-compose down
docker-compose up -d
Elimina volúmenes si es necesario: Si necesitas reiniciar completamente la base de datos:
docker-compose down -v