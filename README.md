# Project for Green Atom

---

![greenatom](https://avatars.mds.yandex.net/i?id=1891506b218b46016d31557cbea846a5_l-5468635-images-thumbs&n=13)

---
## Stack technologies

---

- Java SE 17
- Spring/Spring boot 3.x
- PostgreSQL
- Data JPA 
- Liquibase
- JUnit, Mocktio
- Testcontainers
- Docker

---

## Start project

---

```shell
docker-compose up --build
```

---

## Base endpoints

---

### GET /

Get all files

Example response

```json
[
  {
    "title": "New file",
    "creation_date": "2024-09-07T15:38:55.417413Z",
    "description": "this description new file"
  }
]
```

### GET /{id}

**Request params:**
- `id` (integer, path): get by file ID.

**Response example**

If ID exists - HTTP 200 OK

```json
{
  "fileId": 1,
  "title": "New File",
  "creation_date": "2024-09-07T15:38:55.417413Z",
  "description": "this description new file",
  "file": "/9j/4AAQSkZJRgABAQEASABIAAD..."
}
```

If ID not exists - HTTP 404 Not Found

### POST

/ - add new file

**Request example**

```json
{
  "title": "New File 2",
  "description": "description for new file 2",
  "file": "42DSFGgGAFgafDG//"
}
```

**Response example**

```json
{
  "fileId": 2
}
```

### DELETE /{id}

**Request params:**
- `id` (integer, path): file ID for delete.

**Response example**

If ID exists - **HTTP 200 OK**

If ID not exists, then - **HTTP 404 Not Found**