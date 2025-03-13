import requests
import json
from datetime import datetime
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/api"

headers = {
    "Content-Type": "application/json"
}

# Authentification ADMIN (adapter si besoin)
admin_auth = HTTPBasicAuth('admin', 'admin')

# Authentification READER (à configurer après création)
reader_auth = HTTPBasicAuth('jdupont', 'password123')

# --- Fonction helpers ---

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
    except Exception:
        print("Pas de JSON. Voici la réponse brute :")
        print(response.text)

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

def put_with_admin_auth(url, payload):
    response = requests.put(url, headers=headers, json=payload, auth=admin_auth)
    print("PUT", url)
    print("Status Code:", response.status_code)
    print_json(response)
    return response

def delete_with_admin_auth(url):
    response = requests.delete(url, headers=headers, auth=admin_auth)
    print("DELETE", url)
    print("Status Code:", response.status_code)
    print(response.text)

def safe_get(response, key):
    if response.status_code in [200, 201]:
        return response.json().get(key)
    return None

def search_user_by_username(username):
    print(f"Recherche de User par username '{username}'")
    response = requests.get(
        f"{BASE_URL}/users/search/username",
        params={"username": username},
        headers=headers,
        auth=admin_auth  # Ajout de l'authentification
    )
    
    print("Status Code:", response.status_code)
    
    if response.status_code == 200:
        print_json(response)
        return response.json()
    elif response.status_code == 401:
        print("Erreur : Accès non autorisé. Vérifiez les identifiants administrateurs.")
        return None
    else:
        print(f"Erreur inattendue : {response.status_code}")
        return None

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

def create_user_if_not_exists(user_name, user_username, user_password, role):
    existing_user = search_user_by_username(user_username)
    
    if existing_user:
        print(f"L'utilisateur '{user_name}' avec le username '{user_username}' existe déjà (ID: {existing_user['userId']}). Aucune création nécessaire.")
        return existing_user["userId"]
    
    print("Création d'un Utilisateur")
    user_payload = {
        "userName": user_name,
        "userUsername": user_username,
        "userPassword": user_password,
        "role": role
    }
    response = post_with_admin_auth(f"{BASE_URL}/users", user_payload)
    return safe_get(response, "userId")

# =====================
# DÉBUT DES TESTS
# =====================
print("==============================")
print("DÉMARRAGE DES TESTS COMPLETS API BOOK")
print("==============================\n")

# =====================
# CRÉATION DES DÉPENDANCES
# =====================

print("Création ou récupération du Type")
type_id = create_type_if_not_exists("Roman")

print("Création ou récupération du Genre")
genre_id = create_genre_if_not_exists("Aventure")

print("Création de l'Auteur")
author_payload = {
    "authorFirstName": "Albert",
    "authorLastName": "Camus"
}
response = post_with_admin_auth(f"{BASE_URL}/authors", author_payload)
author_id = safe_get(response, "authorId")

print("Création ou récupération de l'Éditeur")
editor_id = create_editor_if_not_exists("Gallimard", 12345678901234, type_id)

# =====================
# UTILISATEUR
# =====================

print("Création ou récupération d'un Utilisateur READER")
user_id = create_user_if_not_exists("kevin pierre", "kpierre", "password1234", "READER")

# =====================
# BOOK
# =====================

print("Création du Book")
book_payload = {
    "bookTitle": "L'Étranger",
    "bookPublicationDate": "1942-06-01",
    "editor": { "editorId": editor_id },
    "author": { "authorId": author_id },
    "type": { "typeId": type_id },
    "genres": [ { "genreId": genre_id } ],
    "bookDescription": "Un classique de la littérature.",
    "numberOfPages": 123
}
response = post_with_admin_auth(f"{BASE_URL}/books", book_payload)
book_id = safe_get(response, "isbn")

# =====================
# REVIEW (authentifié en READER)
# =====================

def post_with_reader_auth(url, payload):
    response = requests.post(url, headers=headers, json=payload, auth=reader_auth)
    print("POST (READER)", url)
    print("Status Code:", response.status_code)
    print_json(response)
    return response

print("Création d'une Review sur le Book (READER)")
review_payload = {
    "reviewRate": 5,
    "reviewDescription": "Excellent livre !",
    "user": { "userId": user_id },
    "book": { "isbn": book_id }
}
response = post_with_reader_auth(f"{BASE_URL}/reviews", review_payload)
review_id = safe_get(response, "reviewId")

# =====================
# TEST DES ENDPOINTS BOOK
# =====================

print("\n--- Récupération de tous les Books ---")
response = requests.get(f"{BASE_URL}/books")
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Récupération du Book ID {book_id} ---")
response = requests.get(f"{BASE_URL}/books/{book_id}")
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Mise à jour du Book ID {book_id} ---")
update_payload = {
    "bookTitle": "L'Étranger - Édition Révisée",
    "bookPublicationDate": "1950-01-01",
    "editor": { "editorId": editor_id },
    "author": { "authorId": author_id },
    "type": { "typeId": type_id },
    "genres": [ { "genreId": genre_id } ],
    "bookDescription": "Une version révisée du classique.",
    "numberOfPages": 150
}
put_with_admin_auth(f"{BASE_URL}/books/{book_id}", update_payload)

print("\n--- Compter tous les Books ---")
response = requests.get(f"{BASE_URL}/books/count")
print("Status Code:", response.status_code)
print_json(response)

print("\n--- Recherche de Books par titre 'Étranger' ---")
response = requests.get(f"{BASE_URL}/books/search/title", params={ "title": "Étranger" })
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Récupérer les Books par Author ID {author_id} ---")
response = requests.get(f"{BASE_URL}/books/search/author/{author_id}")
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Récupérer les Books par Genre ID {genre_id} ---")
response = requests.get(f"{BASE_URL}/books/search/genre/{genre_id}")
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Récupérer les Books par Editor ID {editor_id} ---")
response = requests.get(f"{BASE_URL}/books/search/editor/{editor_id}")
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Récupérer les Books par Type ID {type_id} ---")
response = requests.get(f"{BASE_URL}/books/search/type/{type_id}")
print("Status Code:", response.status_code)
print_json(response)

print("\n--- Recherche de Books par mot-clé 'étranger' ---")
response = requests.get(f"{BASE_URL}/books/search/keyword", params={ "keyword": "étranger" })
print("Status Code:", response.status_code)
print_json(response)

print("\n--- Récupérer les Books avec des Reviews ---")
response = requests.get(f"{BASE_URL}/books/reviews/not-empty")
print("Status Code:", response.status_code)
print_json(response)

print("\n--- Récupérer les Books avec une note minimale de 4 ---")
response = requests.get(f"{BASE_URL}/books/top-rated", params={ "minRating": 4 })
print("Status Code:", response.status_code)
print_json(response)

print(f"\n--- Suppression du Book ID {book_id} ---")
delete_with_admin_auth(f"{BASE_URL}/books/{book_id}")

# =====================
# FIN DES TESTS
# =====================

print("\n==============================")
print("FIN DES TESTS API BOOK")
print("==============================")

