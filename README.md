# CEZ spring app

## Uruchamianie

Wymagania:

- JDK Java 17
- Maven (w repozytorium dołączony jest Maven Wrapper)

Komenda uruchamiająca:

```shell
./mvnw spring-boot:run
```

## Korzystanie z API

### Lista endpointów

#### GET `/api/v1/patients`

Zwraca listę wszystkich pacjentów.

```shell
curl -X GET http://localhost:8080/api/v1/patients
```

#### POST `/api/v1/patients`

Dodaje nowego pacjenta (o ile podany numer PESEL nie występuje jeszcze w mapie)

```shell
curl -X POST http://localhost:8080/api/v1/patients \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "name": "Jan", "surname": "Kowalski"}'
```

#### GET `/api/v1/patients/{pesel}`

Zwraca pacjenta o podanym numerze PESEL, lub 404 w przypadku gdy taki pacjent nie istnieje

```shell
curl -X POST http://localhost:8080/api/v1/patients \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "name": "Jan", "surname": "Kowalski"}'
  
curl -X GET http://localhost:8080/api/v1/patients/80070881314
```

#### DELETE `/api/v1/patients/{pesel}`

Usuwa pacjenta o podanym numerze PESEL

```shell
curl -X POST http://localhost:8080/api/v1/patients \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "name": "Jan", "surname": "Kowalski"}'

curl -X DELETE http://localhost:8080/api/v1/patients/80070881314
```

#### POST `/api/v1/prescriptions`

Dodaje nową recepcję dla podanego pacjenta, o ile pacjent o podanym numerze PESEL istnieje.

```shell
curl -X POST http://localhost:8080/api/v1/patients \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "name": "Jan", "surname": "Kowalski"}'
  
curl -X POST http://localhost:8080/api/v1/prescriptions \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "medicineName": "ABC", "doseMilligrams": 500}'
```

#### DELETE `/api/v1/prescriptions/{id}`

Usuwa receptę o podanym identyfikatorze UUID.

#### GET `/api/v1/patients/{pesel}/prescriptions`

Zwraca wszystkie recepty dla pacjenta o podanym numerze PESEL

```shell
curl -X POST http://localhost:8080/api/v1/patients \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "name": "Jan", "surname": "Kowalski"}'
  
curl -X POST http://localhost:8080/api/v1/prescriptions \
  -H 'Content-Type: application/json' \
  -d '{"pesel": "80070881314", "medicineName": "ABC", "doseMilligrams": 500}'
  
curl -X GET http://localhost:8080/api/v1/patients/80070881314/prescriptions
```

## Noty implementacyjne

### Przechowywanie numeru PESEL

Szyfrowanie danych wrażliwych (takich jak PESEL), zapobiegałoby wyciekom danych w przypadku uzyskania dostępu do bazy.
W obecnej aplikacji, dane są przechowywane jedynie w pamięci procesu API, zatem szyfrowanie nie zwiększyło by
bezpieczeństwa danych. W produkcyjnej wersji aplikacji (korzystającej z bazy danych) należałoby dane szyfrować przed
wstawieniem. Warto byłoby też zaimplementować mechanizmy autoryzacji w celu uniemożliwienia osobom nieupoważnionym
pobierania danych wrażliwych.

### /api/v1/patients, /api/v1/patients/{pesel}/prescriptions

W obecnej wersji aplikacji, ze względu na przechowywanie niewielkiego wolumenu pacjentów i recept w pamięci, endpointy w
aplikacji nie zawierają stronicowania. W produkcyjnej wersji aplikacji (korzystającej z bazy danych) endpointy te
powinny mieć zaimplementowane stronicowanie. Dla bardzo dużych ilości pacjentów najprawdopodobnie najlepszym wyborem
byłoby stronicowanie za pomocą kursora po indeksowanej kolumnie. W przypadku recept pacjenta, najprawdopodobniej
wystarczyłoby stronicowanie przy pomocy Pageable.
