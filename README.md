# Croc

AWS SQS integration library

## Setup

### GitHub Secrets
Add these secrets to your GitHub repository:
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

### Local Development
Set environment variables:
```bash
export AWS_ACCESS_KEY_ID=<access_key>
export AWS_SECRET_ACCESS_KEY=<secret_key>
export AWS_REGION=us-east-1
```

## Running Tests
```bash
# Unit tests only
mvn test -Dtest='!*IntegrationTest'

# Integration tests only
mvn test -Dtest='*IntegrationTest'

# All tests
mvn test
```