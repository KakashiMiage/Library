import requests
import json
from datetime import datetime

BASE_URL = "http://localhost:8080/api"

headers = {
    "Content-Type": "application/json"
}

print("==============================")
print("DÉMARRAGE DES TESTS COMPLETS API BOOK")
print("==============================\n")

# --- Fonction helpers ---
def print_json(data):
    print(json.dumps(data, indent=4, ensure_ascii=False))

# Création du Type
print("Création du Type")
type_payload = {
    "typeName": "Roman"
}
response = requests.post(f"{BASE_URL}/types", headers=headers, json=type_payload)
print("Status Code:", response.status_code)
print_json(response.json())
type_id = response.json().get("typeId")

# Création du Genre
print("Création du Genre")
genre_payload = {
    "genreName": "Aventure"
}
response = requests.post(f"{BASE_URL}/genres", headers=headers, json=genre_payload)
print("Status Code:", response.status_code)
print_json(response.json())
genre_id = response.json().get("genreId")

# Création de l'Auteur
print("Création de l'Auteur")
author_payload = {
    "authorFirstName": "Albert",
    "authorLastName": "Camus"
}
response = requests.post(f"{BASE_URL}/authors", headers=headers, json=author_payload)
print("Status Code:", response.status_code)
print_json(response.json())
author_id = response.json().get("authorId")

# Création de l'Éditeur
print("Création de l'Éditeur")
editor_payload = {
    "editorName": "Gallimard",
    "editorSIRET": 12345678901234,
    "types": [
        {"typeId": type_id}
    ]
}
response = requests.post(f"{BASE_URL}/editors", headers=headers, json=editor_payload)
print("Status Code:", response.status_code)
print_json(response.json())
editor_id = response.json().get("editorId")

# Création d'un Utilisateur pour les Reviews
print("Création d'un Utilisateur")
user_payload = {
    "userName": "Jean Dupont",
    "userUsername": "jdupont",
    "userPassword": "password123",
    "role": "READER"
}
response = requests.post(f"{BASE_URL}/users", headers=headers, json=user_payload)
print("Status Code:", response.status_code)
print_json(response.json())
user_id = response.json().get("userId")

# Création du Book
print("Création du Book")
book_payload = {
    "bookTitle": "L'Étranger",
    "bookPublicationDate": "1942-06-01",
    "editor": {"editorId": editor_id},
    "author": {"authorId": author_id},
    "type": {"typeId": type_id},
    "genres": [{"genreId": genre_id}],
    "bookDescription": "Un classique de la littérature.",
    "numberOfPages": 123
}
response = requests.post(f"{BASE_URL}/books", headers=headers, json=book_payload)
print("Status Code:", response.status_code)
print_json(response.json())
book_id = response.json().get("isbn")

# Création d'une Review sur le Book
print("Création d'une Review")
review_payload = {
    "reviewRate": 5,
    "reviewDescription": "Excellent livre !",
    "user": {"userId": user_id},
    "book": {"isbn": book_id}
}
response = requests.post(f"{BASE_URL}/reviews", headers=headers, json=review_payload)
print("Status Code:", response.status_code)
print_json(response.json())
review_id = response.json().get("reviewId")

# ----------------------
# TEST DES ENDPOINTS BOOK
# ----------------------

# Récupération de tous les Books
print("Récupération de tous les Books")
response = requests.get(f"{BASE_URL}/books")
print("Status Code:", response.status_code)
print_json(response.json())

# Récupération d'un Book par ID
print(f"Récupération du Book ID {book_id}")
response = requests.get(f"{BASE_URL}/books/{book_id}")
print("Status Code:", response.status_code)
print_json(response.json())

# Mise à jour du Book
print(f"Mise à jour du Book ID {book_id}")
update_payload = {
    "bookTitle": "L'Étranger - Édition Révisée",
    "bookPublicationDate": "1950-01-01",
    "editor": {"editorId": editor_id},
    "author": {"authorId": author_id},
    "type": {"typeId": type_id},
    "genres": [{"genreId": genre_id}],
    "bookDescription": "Une version révisée du classique.",
    "numberOfPages": 150
}
response = requests.put(f"{BASE_URL}/books/{book_id}", headers=headers, json=update_payload)
print("Status Code:", response.status_code)
print_json(response.json())

# Compter tous les Books
print("Compter tous les Books")
response = requests.get(f"{BASE_URL}/books/count")
print("Status Code:", response.status_code)
print(response.json())

# Recherche par titre
print("Recherche de Books par titre 'Étranger'")
response = requests.get(f"{BASE_URL}/books/search/title", params={"title": "Étranger"})
print("Status Code:", response.status_code)
print_json(response.json())

# Recherche par auteur
print(f"Récupérer les Books par Author ID {author_id}")
response = requests.get(f"{BASE_URL}/books/search/author/{author_id}")
print("Status Code:", response.status_code)
print_json(response.json())

# Recherche par genre
print(f"Récupérer les Books par Genre ID {genre_id}")
response = requests.get(f"{BASE_URL}/books/search/genre/{genre_id}")
print("Status Code:", response.status_code)
print_json(response.json())

# Recherche par éditeur
print(f"Récupérer les Books par Editor ID {editor_id}")
response = requests.get(f"{BASE_URL}/books/search/editor/{editor_id}")
print("Status Code:", response.status_code)
print_json(response.json())

# Recherche par type
print(f"Récupérer les Books par Type ID {type_id}")
response = requests.get(f"{BASE_URL}/books/search/type/{type_id}")
print("Status Code:", response.status_code)
print_json(response.json())

# Recherche par mot-clé
print("Recherche de Books par mot-clé 'étranger'")
response = requests.get(f"{BASE_URL}/books/search/keyword", params={"keyword": "étranger"})
print("Status Code:", response.status_code)
print_json(response.json())

# Récupérer les Books avec Reviews
print("Récupérer les Books avec des Reviews")
response = requests.get(f"{BASE_URL}/books/reviews/not-empty")
print("Status Code:", response.status_code)
print_json(response.json())

# Récupérer les Books Top Rated (minRating = 4)
print("Récupérer les Books avec une note minimale de 4")
response = requests.get(f"{BASE_URL}/books/top-rated", params={"minRating": 4})
print("Status Code:", response.status_code)
print_json(response.json())

# Suppression du Book
print(f"Suppression du Book ID {book_id}")
response = requests.delete(f"{BASE_URL}/books/{book_id}")
print("Status Code:", response.status_code)

print("==============================")
print("FIN DES TESTS API BOOK")
print("==============================")
