import requests
import json
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"
headers = {"Content-Type": "application/json"}
admin_auth = HTTPBasicAuth('admin', 'admin')

def print_json(response):
    try:
        print(json.dumps(response.json(), indent=4, ensure_ascii=False))
    except:
        print("Pas de JSON. Réponse brute :")
        print(response.text)

def post(url, payload):
    response = requests.post(url, json=payload, auth=admin_auth)
    print(f"POST {url}\nStatus Code: {response.status_code}")
    print_json(response)
    return response

def put(url, payload):
    response = requests.put(url, json=payload, auth=admin_auth)
    print(f"PUT {url}\nStatus Code: {response.status_code}")
    print_json(response)
    return response

def get(url, params=None):
    response = requests.get(url, params=params)
    print(f"GET {url}\nStatus Code: {response.status_code}")
    print_json(response)
    return response

def delete(url):
    response = requests.delete(url, auth=admin_auth)
    print(f"DELETE {url}\nStatus Code: {response.status_code}")
    print(response.text)

def safe_get(response, key):
    if response.status_code in [200, 201]:
        return response.json().get(key)
    return None

# ==============================
# TEST COMPLET API EDITORS
# ==============================
print("\n==============================")
print("DÉMARRAGE TEST COMPLET API EDITORS")
print("==============================\n")

# Création d'un Type
type_resp = post(f"{BASE_URL}/types", {"typeName": "Essai"})
type_id = safe_get(type_resp, "typeId")

# Création Éditeur avec Type associé
editor_resp = post(f"{BASE_URL}/editors", {
    "editorName": "Gallimard",
    "editorSIRET": 12345678901234,
    "types": [{"typeId": type_id}]
})
editor_id = safe_get(editor_resp, "editorId")

# Récupération tous les éditeurs
get(f"{BASE_URL}/editors")

# Éditeur par ID
get(f"{BASE_URL}/editors/{editor_id}")

# Mise à jour éditeur
put(f"{BASE_URL}/editors/{editor_id}", {
    "editorName": "Gallimard Révisé",
    "editorSIRET": 98765432101234,
    "types": [{"typeId": type_id}]
})

# Recherche éditeur par nom exact
get(f"{BASE_URL}/editors/search/name", params={"name": "Gallimard Révisé"})

# Recherche éditeurs par mot-clé
get(f"{BASE_URL}/editors/search", params={"keyword": "Gallimard"})

# Recherche éditeurs par Type
get(f"{BASE_URL}/editors/search/type/{type_id}")

# Création Genre (dépendance pour Book)
genre_resp = post(f"{BASE_URL}/genres", {"genreName": "Philosophie"})
genre_id = safe_get(genre_resp, "genreId")

# Création Auteur (dépendance Book)
author_resp = post(f"{BASE_URL}/authors", {
    "authorFirstName": "René",
    "authorLastName": "Descartes"
})
author_id = safe_get(author_resp, "authorId")

# Création Livre associé à l'éditeur
book_resp = post(f"{BASE_URL}/books", {
    "bookTitle": "Discours de la méthode",
    "bookPublicationDate": "1637-01-01",
    "editor": {"editorId": editor_id},
    "author": {"authorId": author_id},
    "type": {"typeId": type_id},
    "genres": [{"genreId": genre_id}],
    "bookDescription": "Classique de René Descartes.",
    "numberOfPages": 250
})

# Livres par éditeur
get(f"{BASE_URL}/editors/{editor_id}/books")

# Suppression éditeur
delete(f"{BASE_URL}/editors/{editor_id}")

# Vérification suppression (doit renvoyer 404)
get(f"{BASE_URL}/editors/{editor_id}")

print("\n==============================")
print("FIN DES TESTS API EDITORS")
print("==============================\n")
