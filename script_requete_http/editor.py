import requests
import json
from requests.auth import HTTPBasicAuth
import random

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

def get_with_admin_auth(url, params=None):
    response = requests.get(url, headers=headers, params=params, auth=admin_auth)
    print("GET", url)
    print("Status Code:", response.status_code)
    print_json(response)
    return response

def post_with_admin_auth(url, payload):
    response = requests.post(url, headers=headers, json=payload, auth=admin_auth)
    print("POST", url)
    print("Status Code:", response.status_code)
    print_json(response)
    return response

def create_type_if_not_exists(type_name):
    response = requests.get(f"{BASE_URL}/types/search/name", params={"typeName": type_name}, headers=headers, auth=admin_auth)

    if response.status_code == 200:
        existing_type = response.json()
        if "typeId" in existing_type:
            print(f"Le type '{type_name}' existe déjà avec l'ID {existing_type['typeId']}. Aucune création nécessaire.")
            return existing_type["typeId"]

    print("Création du Type")
    response = requests.post(f"{BASE_URL}/types", headers=headers, json={"typeName": type_name}, auth=admin_auth)
    
    return safe_get(response, "typeId")

def create_editor_if_not_exists(editor_name, editor_siret, type_id):
    # Recherche par nom
    response_name = requests.get(f"{BASE_URL}/editors/search/name", params={"name": editor_name}, headers=headers, auth=admin_auth)
    if response_name.status_code == 200:
        existing_editors = response_name.json()
        if isinstance(existing_editors, list) and existing_editors:
            for editor in existing_editors:
                if editor.get("editorSIRET") == editor_siret:
                    print(f"L'éditeur '{editor_name}' avec le SIRET '{editor_siret}' existe déjà (ID: {editor['editorId']}). Aucune création nécessaire.")
                    return editor["editorId"]

    # Recherche par SIRET
    response_siret = requests.get(f"{BASE_URL}/editors/search/siret", params={"siret": editor_siret}, headers=headers, auth=admin_auth)
    if response_siret.status_code == 200:
        existing_editor_by_siret = response_siret.json()
        if "editorId" in existing_editor_by_siret:
            print(f"L'éditeur avec le SIRET '{editor_siret}' existe déjà (ID: {existing_editor_by_siret['editorId']}). Aucune création nécessaire.")
            return existing_editor_by_siret["editorId"]

    # Création si aucun éditeur n'a été trouvé
    print("Création de l'Éditeur")
    editor_payload = {
        "editorName": editor_name,
        "editorSIRET": editor_siret,
        "types": [{"typeId": type_id}]
    }
    response = post_with_admin_auth(f"{BASE_URL}/editors", editor_payload)
    return safe_get(response, "editorId")

def generate_random_siret():
    return random.randint(10000000000000, 99999999999999)

def create_genre_if_not_exists(genre_name):
    response = requests.get(f"{BASE_URL}/genres/search", params={"name": genre_name}, headers=headers, auth=admin_auth)

    if response.status_code == 200:
        existing_genre = response.json()
        if "genreId" in existing_genre:
            print(f"Le genre '{genre_name}' existe déjà avec l'ID {existing_genre['genreId']}. Aucune création nécessaire.")
            return existing_genre["genreId"]

    print("Création du Genre")
    response = requests.post(f"{BASE_URL}/genres", headers=headers, json={"genreName": genre_name}, auth=admin_auth)
    
    return safe_get(response, "genreId")

# ==============================
# TEST COMPLET API EDITORS
# ==============================
print("\n==============================")
print("DÉMARRAGE TEST COMPLET API EDITORS")
print("==============================\n")

# Création d'un Type
type_id = create_type_if_not_exists("Essai")

# Création Éditeur avec Type associé
editor_id = create_editor_if_not_exists("Gallimard", 12345678901234, type_id)


# Récupération tous les éditeurs
get(f"{BASE_URL}/editors")

# Éditeur par ID
get(f"{BASE_URL}/editors/{editor_id}")

# Mise à jour éditeur
put(f"{BASE_URL}/editors/{editor_id}", {
    "editorName": "Gallimard Révisé",
    "editorSIRET": generate_random_siret(),
    "types": [{"typeId": type_id}]
})

# Recherche éditeur par nom exact
get(f"{BASE_URL}/editors/search/name", params={"name": "Gallimard Révisé"})

# Recherche éditeurs par mot-clé
get(f"{BASE_URL}/editors/search", params={"keyword": "Gallimard"})

# Recherche éditeurs par Type
get(f"{BASE_URL}/editors/search/type/{type_id}")

# Création Genre (dépendance pour Book)
genre_id = create_genre_if_not_exists("Philosophie")

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
