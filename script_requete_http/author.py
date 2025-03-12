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
type_id = create_type_if_not_exists("Roman")

# 6. Création Genre
genre_id = create_genre_if_not_exists("Drame")

# 7. Création Éditeur
editor_id = create_editor_if_not_exists("Éditions Classiques", 98765432101234, type_id)

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
