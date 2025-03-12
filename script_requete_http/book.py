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

# =====================
# DÉBUT DES TESTS
# =====================
print("==============================")
print("DÉMARRAGE DES TESTS COMPLETS API BOOK")
print("==============================\n")

# =====================
# CRÉATION DES DÉPENDANCES
# =====================

print("Création du Type")
type_payload = { "typeName": "Roman" }
response = post_with_admin_auth(f"{BASE_URL}/types", type_payload)
type_id = safe_get(response, "typeId")

print("Création du Genre")
genre_payload = { "genreName": "Aventure" }
response = post_with_admin_auth(f"{BASE_URL}/genres", genre_payload)
genre_id = safe_get(response, "genreId")

print("Création de l'Auteur")
author_payload = {
    "authorFirstName": "Albert",
    "authorLastName": "Camus"
}
response = post_with_admin_auth(f"{BASE_URL}/authors", author_payload)
author_id = safe_get(response, "authorId")

print("Création de l'Éditeur")
editor_payload = {
    "editorName": "Gallimard",
    "editorSIRET": 12345678901234,
    "types": [ { "typeId": type_id } ]
}
response = post_with_admin_auth(f"{BASE_URL}/editors", editor_payload)
editor_id = safe_get(response, "editorId")

# =====================
# UTILISATEUR
# =====================

print("Création d'un Utilisateur READER")
user_payload = {
    "userName": "kevin pierre",
    "userUsername": "kpierre",
    "userPassword": "password1234",
    "role": "READER"
}
response = post_with_admin_auth(f"{BASE_URL}/users", user_payload)
user_id = safe_get(response, "userId")

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

