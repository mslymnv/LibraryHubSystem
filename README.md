services:

  user-postgres:
    image: postgres:17
    container_name: mypostgresforuser
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
      POSTGRES_DB: user_db
    ports:
      - "5432:5432"
    volumes:
      - user_postgres_data:/var/lib/postgresql/data
      
  book-postgres:
    image: postgres:17
    container_name: mypostgresforbook
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
      POSTGRES_DB: book_db
    ports:
      - "5433:5432"
    volumes:
      - book_postgres_data:/var/lib/postgresql/data
  redis:
    image: redis:7-alpine
    container_name: myredis
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3-management
    container_name: myrabbitmq
    ports:
      - "5672:5672"
      - "15672:15672"

  user_service:
    build: ./user-service
    container_name: user_service
    ports:
      - "8081:8081"
    environment:
      SPRING_PROFILES_ACTIVE: dev
    depends_on:
      - user-postgres
      - redis
      - rabbitmq

  book-service:
    build: ./book-service
    container_name: book-service
    ports:
      - "8082:8082"
    environment:
      SPRING_PROFILES_ACTIVE: dev
    depends_on:
      - book-postgres
      - redis
      - rabbitmq

volumes:
  user_postgres_data:
  book_postgres_data:
