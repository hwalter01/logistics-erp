services:
  postgres:
    image: postgres:16
    container_name: fahrtentracker-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: logistics
      POSTGRES_USER: logistics
      POSTGRES_PASSWORD: V0v40w&8vzd+
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init:/docker-entrypoint-initdb.d

volumes:
  postgres_data: