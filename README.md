# 📝 Сохранение данных формуляра в базе данных с помощью сервлетов и Docker Compose

Этот проект демонстрирует, как создать и сохранить данные из формуляра (HTML-страницы) в базу данных MySQL с
использованием Java Servlets, JPA/Hibernate и Docker Compose. Ключевым моментом является интеграция серверной логики с
контейнеризированной базой данных MySQL.

---

## 📌 Что в этом проекте:

- **Отправка данных формы**: С использованием Fetch API и JSON.
- **Сохранение в базу данных**: MySQL через JPA/Hibernate.
- **Использование Docker Compose**: Для автоматической настройки базы данных MySQL.
- **Минимальная конфигурация сервлета**: Простое управление HTTP-запросами.

---

## 🛠️ Используемые технологии:

- **Java**
- **Jakarta EE (Servlets, JPA)**
- **Hibernate ORM**
- **MySQL**
- **Docker Compose**
- **HTML, CSS, JavaScript (Fetch API)**

---

## 🚀 Как запустить проект:

### 1. Склонируйте репозиторий:

```bash
git clone https://github.com/javafullstackdeveloper2685/video_9_save_form_in_db.git
cd ваш-репозиторий
```

### 2. Соберите проект с Maven:

```bash
mvn clean package
```

### 3. Запустите базу данных через Docker Compose:

Убедитесь, что Docker установлен:

```bash
docker-compose up -d
```

После запуска MySQL доступен на порту ``3306``.

### 4. Разверните проект на сервере Tomcat:

- Переместите собранный файл WAR из target/servletsTest.war в папку webapps вашего Tomcat.
- Убедитесь, что сервер запущен.

### 5. Откройте приложение в браузере:

Перейдите по адресу:

```bash
http://localhost:8080/servletsTest/index.html
```
## 📂 Структура проекта
```csharp
.
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── org.game/
│ │ │ │ ├── entities/
│ │ │ │ │ └── SForm.java # JPA Entity-класс
│ │ │ │ ├── model/
│ │ │ │ │ ├── RequestData.java # DTO для запроса
│ │ │ │ │ └── ResponseData.java # DTO для ответа
│ │ │ │ └── servlets/
│ │ │ │ └── MainServlet.java # Главный сервлет
│ │ ├── resources/
│ │ │ └── META-INF/
│ │ │ └── persistence.xml # Конфигурация JPA
│ │ ├── webapp/
│ │ ├── WEB-INF/
│ │ │ └── web.xml # Настройки сервлетов
│ │ ├── index.html # Форма отправки данных
│ │ └── js/
│ │ └── script.js # Fetch API для отправки формы
│ └── test/
│       ├── java/
│       │   └── org.game/
│       │               ├── entities/
│       │               │   └── SFormPersistenceTest.java
│       │               └── servlets/
│       │                   └── MainServletTest.java
│       └── resources/
│           └── META-INF/
│               └── persistence.xml
├── docker-compose.yml # Docker Compose для запуска MySQL
├── init.sql # SQL-скрипт для создания таблицы
├── pom.xml # Конфигурация Maven
└── README.md # Этот файл
```
## 📑 Пример работы
### HTML-форма:
```html
<form id="messageForm">
    <label for="name">Имя:</label>
    <input type="text" id="name" name="name" required>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required>

    <label for="message">Сообщение:</label>
    <textarea id="message" name="message" required></textarea>

    <button type="submit">Отправить</button>

</form>
<div id="output"></div>
```
### JavaScript (Fetch API):
```javascript
document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('#messageForm');
    const output = document.querySelector('#output');

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const dataToSend = Object.fromEntries(formData.entries());

        fetch('/game', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dataToSend),
        })
        .then(response => response.json())
        .then(data => {
            output.innerHTML = `<p>Статус: ${data.status}</p><p>Сообщение: ${data.echoMessage}</p>`;
        })
        .catch(error => {
            output.innerHTML = `<p>Ошибка: ${error.message}</p>`;
        });
    });

});
```
## 🛠️ Конфигурация JPA (persistence.xml):
```xml
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
version="3.0">
<persistence-unit name="messages-persistence-unit">
<class>org.game.entities.SForm</class>
<properties>
<property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://db:3306/messages"/>
<property name="jakarta.persistence.jdbc.user" value="user"/>
<property name="jakarta.persistence.jdbc.password" value="password"/>
<property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect"/>
<property name="hibernate.hbm2ddl.auto" value="update"/>
<property name="hibernate.show_sql" value="true"/>
</properties>
</persistence-unit>
</persistence>
```
## 📦 Конфигурация Docker Compose:
``` yaml
version: '3.8'

services:
db:
image: mysql:8.0
container_name: mysql_db
restart: always
environment:
MYSQL_ROOT_PASSWORD: root
MYSQL_DATABASE: messages
MYSQL_USER: user
MYSQL_PASSWORD: password
ports:

- "3306:3306"
  volumes:
- db_data:/var/lib/mysql
- ./init.sql:/docker-entrypoint-initdb.d/init.sql

volumes:
db_data:
```
## 💡 Удачи в программировании! Если у вас есть вопросы, оставляйте комментарии. 🚀