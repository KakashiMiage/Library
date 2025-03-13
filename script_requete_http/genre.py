import requests
import json
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"  # Adapt if needed
admin_auth = HTTPBasicAuth('admin', 'admin')

headers = {
    "Content-Type": "application/json"
}

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

print("\n==============================")
print("DÉMARRAGE TEST COMPLET API GENRES")
print("==============================\n")

# --- 1) Création Genre
genre_id = create_genre_if_not_exists("Philosophie")

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
type_id = create_type_if_not_exists("Essai")

author_resp = post(f"{BASE_URL}/authors", {
    "authorFirstName": "Sigmund",
    "authorLastName": "Freud"
})
author_id = safe_get(author_resp, "authorId")

editor_id = create_editor_if_not_exists("Éditions Psychanalytiques", 1234567890, type_id)

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
