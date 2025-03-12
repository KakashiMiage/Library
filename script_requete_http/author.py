import requests
import json
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"
headers = {"Content-Type": "application/json"}
admin_auth = HTTPBasicAuth('admin', 'admin')

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
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

def get(url):
    response = requests.get(url)
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

# --- TEST COMPLET AUTHORS ---
print("\n==============================")
print("DÉMARRAGE TEST COMPLET API AUTHORS")
print("==============================\n")

# 1. Création Auteur
author_resp = post(f"{BASE_URL}/authors", {
    "authorFirstName": "Victor",
    "authorLastName": "Hugo"
})
author_id = safe_get(author_resp, "authorId")

# 2. Récupérer tous les auteurs
get(f"{BASE_URL}/authors")

# 3. Auteur par ID
get(f"{BASE_URL}/authors/{author_id}")

# 2. Recherche par nom complet
get(f"{BASE_URL}/authors/search/fullname?firstName=Victor&lastName=Hugo")

# 3. Recherche par nom de famille
get(f"{BASE_URL}/authors/search/lastname/Hugo")

# 4. Compter tous les auteurs
get(f"{BASE_URL}/authors/count")

# 5. Mise à jour Auteur
update_resp = put(f"{BASE_URL}/authors/{author_id}", {
    "authorFirstName": "Victor-Marie",
    "authorLastName": "Hugo"
})

# 6. Création Type
type_resp = post(f"{BASE_URL}/types", {"typeName": "Roman"})
type_id = safe_get(type_resp, "typeId")

# 6. Création Genre
genre_resp = post(f"{BASE_URL}/genres", {"genreName": "Drame"})
genre_id = safe_get(genre_resp, "genreId")

# 7. Création Éditeur
editor_resp = post(f"{BASE_URL}/editors", {
    "editorName": "Éditions Classiques",
    "editorSIRET": 98765432101234,
    "types": [{"typeId": type_id}]
})
editor_id = safe_get(editor_resp, "editorId")

# 8. Création Livre (lié à l'auteur)
book_resp = post(f"{BASE_URL}/books", {
    "bookTitle": "Les Misérables",
    "bookPublicationDate": "1862-04-03",
    "editor": {"editorId": editor_id},
    "author": {"authorId": author_id},
    "type": {"typeId": type_id},
    "genres": [{"genreId": genre_id}],
    "bookDescription": "Chef d'œuvre de Victor Hugo.",
    "numberOfPages": 1463
})

# 8. Livres de l'auteur
get(f"{BASE_URL}/authors/{author_id}/books")

# 9. Suppression Auteur
delete(f"{BASE_URL}/authors/{author_id}")

# 10. Vérifier suppression Auteur (doit retourner 404)
get(f"{BASE_URL}/authors/{author_id}")

print("\n==============================")
print("FIN DES TESTS API AUTHORS")
print("==============================\n")
