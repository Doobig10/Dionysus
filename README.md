# Dionysus - Final Year Project
## Project Description

[Project Description]

## Component Description
Dionysus-Master functions as the networks central server, handling all node requests as well as database access. 
Many master servers can run on the same network provided they all utilise different ports for their working nodes.


## Example Compose File (All Components)
```yaml
version: "3.0"

services:
  db:
    image: mariadb
    container_name: dionysus-db        
    volumes:
      - dionysus-db:/var/lib/mysql
  
  master:
    image: image
    container_name: dionysus-master
    environment:
      ENV: true
    volumes:
      - ./example-directory:./configs
        
  node:
    image: image
    container_name: dionysus-node
    environment:
      ENV: true
    volumes:
      - ./example-directory:./configs


volumes:
  dionysus-db:
```

