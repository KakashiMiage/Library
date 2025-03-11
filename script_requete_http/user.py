import requests
import json

BASE_URL_USER = "http://localhost:8080/api/users"
BASE_URL_AUTHOR = "http://localhost:8080/api/authors"
BASE_URL_EDITOR = "http://localhost:8080/api/editors"
BASE_URL_TYPE = "http://localhost:8080/api/types"
BASE_URL_GENRE = "http://localhost:8080/api/genres"
BASE_URL_BOOK = "http://localhost:8080/api/books"
BASE_URL_REVIEW = "http://localhost:8080/api/reviews"

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
    except Exception as e:
        print(f"Erreur de parsing JSON: {e}")
        print(response.text)

# ==== Dépendances ====
def create_type(type_name):
    print("Création d'un Type")
    payload = { "typeName": type_name }
    response = requests.post(BASE_URL_TYPE, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("typeId") if response.status_code == 201 else None

def create_genre(genre_name):
    print("Création d'un Genre")
    payload = { "genreName": genre_name }
    response = requests.post(BASE_URL_GENRE, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("genreId") if response.status_code == 201 else None

def create_author(first_name, last_name):
    print("Création d'un Auteur")
    payload = { "authorFirstName": first_name, "authorLastName": last_name }
    response = requests.post(BASE_URL_AUTHOR, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("authorId") if response.status_code == 201 else None

def create_editor(editor_name, siret, type_id):
    print("Création d'un Éditeur")
    payload = {
        "editorName": editor_name,
        "editorSIRET": siret,
        "types": [ { "typeId": type_id } ]
    }
    response = requests.post(BASE_URL_EDITOR, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("editorId") if response.status_code == 201 else None

def create_book(title, pub_date, author_id, editor_id, type_id, genre_id):
    print("Création d'un Book")
    payload = {
        "bookTitle": title,
        "bookPublicationDate": pub_date,
        "author": { "authorId": author_id },
        "editor": { "editorId": editor_id },
        "type": { "typeId": type_id },
        "genres": [ { "genreId": genre_id } ],
        "bookDescription": "Un livre fascinant",
        "numberOfPages": 300
    }
    response = requests.post(BASE_URL_BOOK, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("isbn") if response.status_code == 201 else None

def create_review(user_id, book_id, rate, description):
    print("Création d'une Review")
    payload = {
        "reviewRate": rate,
        "reviewDescription": description,
        "user": { "userId": user_id },
        "book": { "isbn": book_id }
    }
    response = requests.post(BASE_URL_REVIEW, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("reviewId") if response.status_code == 201 else None

# ==== USERS ====
def create_user(name, username, password, role):
    print("Création d'un Utilisateur")
    payload = {
        "userName": name,
        "userUsername": username,
        "userPassword": password,
        "role": role
    }
    response = requests.post(BASE_URL_USER, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("userId") if response.status_code == 201 else None

def get_all_users():
    print("Récupération de tous les Users")
    response = requests.get(BASE_URL_USER)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_user_by_id(user_id):
    print(f"Récupération du User ID {user_id}")
    response = requests.get(f"{BASE_URL_USER}/{user_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def update_user(user_id, name, username, password, role, favorite_books):
    print(f"Mise à jour du User ID {user_id}")
    payload = {
        "userName": name,
        "userUsername": username,
        "userPassword": password,
        "role": role,
        "favoriteBooks": favorite_books
    }
    response = requests.put(f"{BASE_URL_USER}/{user_id}", json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def delete_user(user_id):
    print(f"Suppression du User ID {user_id}")
    response = requests.delete(f"{BASE_URL_USER}/{user_id}")
    print(f"Status Code: {response.status_code}")

def count_users():
    print("Comptage des Users")
    response = requests.get(f"{BASE_URL_USER}/count")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def search_user_by_name(name):
    print(f"Recherche de Users par nom '{name}'")
    response = requests.get(f"{BASE_URL_USER}/search/name", params={"name": name})
    print(f"Status Code: {response.status_code}")
    print_json(response)

def search_user_by_username(username):
    print(f"Recherche de User par username '{username}'")
    response = requests.get(f"{BASE_URL_USER}/search/username", params={"username": username})
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_reviews_by_user(user_id):
    print(f"Récupération des Reviews du User ID {user_id}")
    response = requests.get(f"{BASE_URL_USER}/{user_id}/reviews")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_books_reviewed_by_user(user_id):
    print(f"Récupération des Books reviewés par le User ID {user_id}")
    response = requests.get(f"{BASE_URL_USER}/{user_id}/books-reviewed")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def add_book_to_favorites(user_id, book_id):
    print(f"Ajout du Book ID {book_id} aux favoris du User ID {user_id}")
    response = requests.put(f"{BASE_URL_USER}/{user_id}/favorites/{book_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def remove_book_from_favorites(user_id, book_id):
    print(f"Suppression du Book ID {book_id} des favoris du User ID {user_id}")
    response = requests.delete(f"{BASE_URL_USER}/{user_id}/favorites/{book_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_favorite_books(user_id):
    print(f"Récupération des favoris du User ID {user_id}")
    response = requests.get(f"{BASE_URL_USER}/{user_id}/favorites")
    print(f"Status Code: {response.status_code}")
    print_json(response)

# =============================
# TEST RUNNER
# =============================
def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API USER")
    print("==============================")

    # Créer les dépendances pour tester les favoris et reviews
    type_id = create_type("Roman")
    genre_id = create_genre("Aventure")
    author_id = create_author("Victor", "Hugo")
    editor_id = create_editor("Plume Editions", 12345678901234, type_id)
    book_id = create_book("Les Misérables", "1862-04-03", author_id, editor_id, type_id, genre_id)

    if not all([type_id, genre_id, author_id, editor_id, book_id]):
        print("Erreur lors de la création des dépendances. Arrêt des tests.")
        return

    # Création d'un utilisateur
    user_id = create_user("Jean Valjean", "valjean", "password", "READER")
    if not user_id:
        print("Erreur lors de la création du user. Arrêt des tests.")
        return

    # Ajouter une review
    review_id = create_review(user_id, book_id, 5, "Chef-d'œuvre intemporel.")
    if not review_id:
        print("Erreur lors de la création de la review.")

    # Ajouter le livre en favori via update_user
    update_user(user_id, "Jean Valjean", "valjean_updated", "newpass", "READER", [{"isbn": book_id}])

    # Ajout d'un livre favori avec endpoint dédié
    add_book_to_favorites(user_id, book_id)

    # Récupération des favoris
    get_favorite_books(user_id)

    # Suppression du favori
    remove_book_from_favorites(user_id, book_id)

    # Vérifications
    get_all_users()
    get_user_by_id(user_id)
    search_user_by_name("Jean Valjean")
    search_user_by_username("valjean_updated")
    count_users()

    # Reviews et livres reviewés
    get_reviews_by_user(user_id)
    get_books_reviewed_by_user(user_id)

    # Suppression du user
    delete_user(user_id)
    get_user_by_id(user_id)

    print("==============================")
    print("FIN DES TESTS API USER")
    print("==============================")

if __name__ == "__main__":
    run_tests()
