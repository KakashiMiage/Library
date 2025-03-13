import requests
import json
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"
admin_auth = HTTPBasicAuth('admin', 'admin')

headers = {
    "Content-Type": "application/json"
}

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

def create_genre_with_type_if_not_exists(genre_name, type_id):
    response = requests.get(f"{BASE_URL}/genres/search", params={"name": genre_name}, headers=headers, auth=admin_auth)

    if response.status_code == 200:
        existing_genre = response.json()
        if "genreId" in existing_genre:
            genre_id = existing_genre["genreId"]
            print(f"Le genre '{genre_name}' existe déjà avec l'ID {genre_id}. Vérification du type associé...")
            
            # Vérifier si le type est déjà associé
            existing_types = existing_genre.get("types", [])
            existing_type_ids = {t["typeId"] for t in existing_types}

            if type_id in existing_type_ids:
                print(f"Le type '{type_id}' est déjà associé au genre '{genre_name}'. Aucune modification nécessaire.")
            else:
                print(f"Ajout du type '{type_id}' au genre '{genre_name}'.")
                updated_types = existing_types + [{"typeId": type_id}]
                
                update_payload = {
                    "genreName": genre_name,
                    "types": updated_types
                }
                response = requests.put(f"{BASE_URL}/genres/{genre_id}", json=update_payload, headers=headers, auth=admin_auth)
                
                if response.status_code in [200, 204]:
                    print(f"Type ajouté avec succès au genre '{genre_name}'.")
                else:
                    print(f"Erreur lors de l'ajout du type au genre '{genre_name}': {response.status_code}")
            
            return genre_id

    # Création du genre avec le type
    print(f"Création du Genre '{genre_name}' avec le type '{type_id}'.")
    genre_payload = {
        "genreName": genre_name,
        "types": [{"typeId": type_id}]
    }
    response = requests.post(f"{BASE_URL}/genres", json=genre_payload, headers=headers, auth=admin_auth)
    
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

def update_type_if_needed(type_id, new_type_name):
    # Vérifier si le nouveau nom existe déjà
    response = requests.get(f"{BASE_URL}/types/search/name", params={"typeName": new_type_name}, headers=headers, auth=admin_auth)

    if response.status_code == 200:
        existing_type = response.json()
        if "typeId" in existing_type:
            print(f"Le type '{new_type_name}' existe déjà avec l'ID {existing_type['typeId']}. Impossible de modifier.")
            return existing_type["typeId"]  # On retourne l'ID du type existant

    # Modifier le type si le nom n'existe pas déjà
    print(f"Le type avec l'ID {type_id} va être mis à jour en '{new_type_name}'.")
    update_payload = {"typeName": new_type_name}
    response = requests.put(f"{BASE_URL}/types/{type_id}", json=update_payload, headers=headers, auth=admin_auth)

    if response.status_code in [200, 204]:
        print(f"Type mis à jour avec succès en '{new_type_name}'.")
        return type_id
    else:
        print(f"Erreur lors de la mise à jour du type : {response.status_code}")
        return None



print("\n==============================")
print("DÉMARRAGE TEST COMPLET API TYPES")
print("==============================\n")

# 1. Créer deux Types
type1_id = create_type_if_not_exists("Essai")

type2_id = create_type_if_not_exists("Biographie")

# 2. Récupérer tous les Types
get(f"{BASE_URL}/types")

# 3. Récupérer un Type par ID
get(f"{BASE_URL}/types/{type1_id}")

# 4. Rechercher Type par nom exact
get(f"{BASE_URL}/types/search/name", {"typeName": "Essai"})

# 5. Mettre à jour le premier Type
update_type_if_needed(type1_id, "Essai Révisé")

# 5. Vérifier mise à jour
get(f"{BASE_URL}/types/{type1_id}")

# 5. Compter les types
get(f"{BASE_URL}/types/count")

# 6. Créer Genre AVEC Type associé
genre_id = create_genre_with_type_if_not_exists("Philosophie", type1_id)

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
    "genres": [{"genreId": genre_id}],
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
get(f"{BASE_URL}/types/search/genre/{genre_id}")

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
