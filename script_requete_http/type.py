import requests
import json
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"
admin_auth = HTTPBasicAuth('admin', 'admin')

def print_json(resp):
    try:
        print(json.dumps(resp.json(), indent=4, ensure_ascii=False))
    except:
        print(resp.text)

def post(url, payload):
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("POST", url, "\nStatus Code:", resp.status_code)
    print_json(resp)
    return resp

def put(url, payload):
    resp = requests.put(url, json=payload, auth=admin_auth)
    print(f"PUT {url}\nStatus: {resp.status_code}")
    print_json(resp)
    return resp

def get(url, params=None):
    resp = requests.get(url, params=params)
    print(f"GET {url}\nStatus: {resp.status_code}")
    print_json(resp)
    return resp

def delete(url):
    resp = requests.delete(url, auth=admin_auth)
    print(f"DELETE {url}\nStatus: {resp.status_code}")
    print(resp.text)

def safe_get(resp, key):
    if resp.status_code in [200, 201]:
        return resp.json().get(key)
    return None

print("\n==============================")
print("DÉMARRAGE TEST COMPLET API TYPES")
print("==============================\n")

# 1. Créer deux Types
resp_type1 = requests.post(f"{BASE_URL}/types", json={"typeName": "Essai"}, auth=admin_auth)
print_json(resp_type1)
type1_id = safe_get(resp_type1, "typeId")

resp_type2 = requests.post(f"{BASE_URL}/types", json={"typeName": "Biographie"}, auth=admin_auth)
type2_id = safe_get(resp_type2, "typeId")

# 2. Récupérer tous les Types
get(f"{BASE_URL}/types")

# 3. Récupérer un Type par ID
get(f"{BASE_URL}/types/{type1_id}")

# 4. Rechercher Type par nom exact
get(f"{BASE_URL}/types/search/name", {"typeName": "Essai"})

# 5. Mettre à jour le premier Type
put(f"{BASE_URL}/types/{type1_id}", {"typeName": "Essai Révisé"})

# 5. Vérifier mise à jour
get(f"{BASE_URL}/types/{type1_id}")

# 5. Compter les types
get(f"{BASE_URL}/types/count")

# 6. Créer Genre AVEC Type associé
genre_resp = requests.post(f"{BASE_URL}/genres", json={
    "genreName": "Philosophie",
    "types": [{"typeId": type1_id}]
}, auth=admin_auth)
genre_id = safe_get(genre_resp, "genreId")

# 7. Créer Auteur
author_resp = requests.post(f"{BASE_URL}/authors", json={
    "authorFirstName": "Friedrich",
    "authorLastName": "Nietzsche"
}, auth=admin_auth)
author_id = safe_get(author_resp, "authorId")

# 7. Créer Éditeur associé au Type
editor_resp = requests.post(f"{BASE_URL}/editors", json={
    "editorName": "Éditions Philosophiques",
    "editorSIRET": 11223344556677,
    "types": [{"typeId": type1_id}]
}, auth=admin_auth)
editor_id = safe_get(editor_resp, "editorId")

# 8. Créer Livre associé au Type
book_resp = requests.post(f"{BASE_URL}/books", json={
    "bookTitle": "Ainsi parlait Zarathoustra",
    "bookPublicationDate": "1883-01-01",
    "genres": [{"genreId": safe_get(genre_resp, "genreId")}],
    "bookDescription": "Œuvre majeure de Nietzsche.",
    "numberOfPages": 300,
    "author": {"authorId": author_id},
    "editor": {"editorId": safe_get(editor_resp, "editorId")},
    "type": {"typeId": type1_id}
}, auth=admin_auth)

book_id = safe_get(book_resp, "isbn")

# 8. Récupérer livres par Type
get(f"{BASE_URL}/types/{type1_id}/books")

# 9. Récupérer les Types liés à un Genre
get(f"{BASE_URL}/types/search/genre/{safe_get(genre_resp, 'genreId')}")

# 10. Supprimer livre associé
requests.delete(f"{BASE_URL}/books/{book_id}", auth=admin_auth)

# 10. Suppression des Types
delete(f"{BASE_URL}/types/{type1_id}")
delete(f"{BASE_URL}/types/{type2_id}")

# Vérifier suppression du Type
get(f"{BASE_URL}/types/{type1_id}")

# Vérifier liste et comptage après suppression
get(f"{BASE_URL}/types")
get(f"{BASE_URL}/types/count")

print("\n==============================")
print("FIN DES TESTS API TYPES")
print("==============================\n")
