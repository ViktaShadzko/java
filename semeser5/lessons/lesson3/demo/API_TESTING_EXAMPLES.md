# API Testing Examples

## Access OpenAPI Documentation

```bash
# Open Swagger UI in browser
start http://localhost:8080/swagger-ui.html

# Get OpenAPI JSON spec
curl http://localhost:8080/v3/api-docs

# Get OpenAPI YAML spec
curl http://localhost:8080/v3/api-docs.yaml
```

## SpaceShip API Examples

### Get All Spaceships
```bash
curl -X GET "http://localhost:8080/api/v1/space/spaceShip" -H "accept: application/json"
```

### Get Spaceship by ID
```bash
curl -X GET "http://localhost:8080/api/v1/space/spaceShip/1" -H "accept: application/json"
```

### Create a New Spaceship
```bash
curl -X POST "http://localhost:8080/api/v1/space/spaceShip" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "USS Enterprise",
    "guns": [
      {
        "damage": 100,
        "noise": "BOOM",
        "ammo": 500
      }
    ],
    "shieldGenerator": {
      "capacity": 1000,
      "rechargeRate": 50
    },
    "engines": {
      "power": 5000,
      "fuelConsumption": 10
    }
  }'
```

### Partially Update Spaceship (PATCH)
```bash
curl -X PATCH "http://localhost:8080/api/v1/space/spaceShip/1" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "USS Enterprise-D"
  }'
```

### Fully Replace Spaceship (PUT)
```bash
curl -X PUT "http://localhost:8080/api/v1/space/spaceShip/1" \
  -H "accept: application/json" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "USS Voyager",
    "guns": [
      {
        "damage": 150,
        "noise": "ZAP",
        "ammo": 300
      }
    ],
    "shieldGenerator": {
      "capacity": 1200,
      "rechargeRate": 60
    },
    "engines": {
      "power": 6000,
      "fuelConsumption": 12
    }
  }'
```

### Delete Spaceship
```bash
curl -X DELETE "http://localhost:8080/api/v1/space/spaceShip/1" -H "accept: application/json"
```

## Welcome Page Examples

### Access Welcome Page (Default)
```bash
curl "http://localhost:8080/welcome"
```

### Access Welcome Page with Custom Name
```bash
curl "http://localhost:8080/welcome?user=Captain"
```

## PowerShell Examples

### Get All Spaceships (PowerShell)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/space/spaceShip" -Method Get
```

### Create Spaceship (PowerShell)
```powershell
$body = @{
    id = 1
    name = "USS Enterprise"
    guns = @(
        @{
            damage = 100
            noise = "BOOM"
            ammo = 500
        }
    )
    shieldGenerator = @{
        capacity = 1000
        rechargeRate = 50
    }
    engines = @{
        power = 5000
        fuelConsumption = 10
    }
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/space/spaceShip" -Method Post -Body $body -ContentType "application/json"
```

### Get OpenAPI Spec (PowerShell)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/v3/api-docs" -Method Get | ConvertTo-Json -Depth 10
```

## Using Swagger UI

1. Start your application
2. Open browser and navigate to: `http://localhost:8080/swagger-ui.html`
3. You will see:
   - All available endpoints grouped by tags
   - Schemas for all entities
   - Try it out functionality for each endpoint
4. To test an endpoint:
   - Click on the endpoint
   - Click "Try it out"
   - Fill in the parameters/body
   - Click "Execute"
   - View the response

## Exporting OpenAPI Spec

### Save as JSON
```bash
curl http://localhost:8080/v3/api-docs -o openapi.json
```

### Save as YAML
```bash
curl http://localhost:8080/v3/api-docs.yaml -o openapi.yaml
```

### PowerShell
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -OutFile "openapi.json"
Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs.yaml" -OutFile "openapi.yaml"
```

