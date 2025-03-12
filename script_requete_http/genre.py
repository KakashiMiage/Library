import requests
import json
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"  # Adapt if needed
admin_auth = HTTPBasicAuth('admin', 'admin')

def print_json(response):
    try:
        print(json.dumps(response.json(), indent=4, ensure_ascii=False))
    except:
        print("Pas de JSON. Réponse brute :")
        print(response.text)

def post(url, payload):
    resp = requests.post(url, json=payload, auth=admin_auth)
    print(f"POST {url}\nStatus: {resp.status_code}")
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

def safe_get(response, key):
    if response.status_code in [200, 201]:
        return response.json().get(key)
    return None

print("\n==============================")
print("DÉMARRAGE TEST COMPLET API GENRES")
print("==============================\n")

# --- 1) Création Genre
genre_resp = post(f"{BASE_URL}/genres", {
    "genreName": "Philosophie"
})
genre_id = safe_get(genre_resp, "genreId")

# --- 2) Récupérer tous les Genres
get(f"{BASE_URL}/genres")

# --- 3) Récupérer Genre par ID
get(f"{BASE_URL}/genres/{genre_id}")

# --- 4) Recherche par nom
search_resp = get(f"{BASE_URL}/genres/search", {"name": "Philosophie"})

# --- 5) Mise à jour du Genre
put(f"{BASE_URL}/genres/{genre_id}", {
    "genreName": "Philosophie Révisée"
})

# --- 6) Vérifier mise à jour
get(f"{BASE_URL}/genres/{genre_id}")

# --- 7) Compter les Genres
get(f"{BASE_URL}/genres/count")

# --- 8) Créer dépendances (Type, Author, Editor) pour Book
type_resp = post(f"{BASE_URL}/types", {"typeName": "Essai"})
type_id = safe_get(type_resp, "typeId")

author_resp = post(f"{BASE_URL}/authors", {
    "authorFirstName": "Sigmund",
    "authorLastName": "Freud"
})
author_id = safe_get(author_resp, "authorId")

editor_resp = post(f"{BASE_URL}/editors", {
    "editorName": "Éditions Psychanalytiques",
    "editorSIRET": 1234567890,
    "types": [{"typeId": type_id}]
})
editor_id = safe_get(editor_resp, "editorId")

# --- 9) Créer un Livre associé à ce Genre
book_resp = post(f"{BASE_URL}/books", {
    "bookTitle": "L'Interprétation du rêve",
    "bookPublicationDate": "1899-01-01",
    "genres": [{"genreId": genre_id}],
    "bookDescription": "Ouvrage fondateur de Freud.",
    "numberOfPages": 300,
    "author": {"authorId": author_id},
    "editor": {"editorId": editor_id},
    "type": {"typeId": type_id}
})
book_id = safe_get(book_resp, "isbn")

# --- 10) Récupérer les livres liés à ce genre
get(f"{BASE_URL}/genres/{genre_id}/books")

# (Optionnel) Supprimer le livre pour tester la suite
delete(f"{BASE_URL}/books/{book_id}")

# --- 11) Suppression du Genre
delete(f"{BASE_URL}/genres/{genre_id}")

# --- 12) Vérifier que le Genre est bien supprimé
get(f"{BASE_URL}/genres/{genre_id}")

# --- 13) Récupérer la liste finale et compter
get(f"{BASE_URL}/genres")
get(f"{BASE_URL}/genres/count")

print("\n==============================")
print("FIN DES TESTS API GENRES")
print("==============================\n")
