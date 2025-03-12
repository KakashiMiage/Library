import requests
import json
from requests.auth import HTTPBasicAuth

# ======================
# Configuration
# ======================
BASE_URL_USER = "http://localhost:8080/api/users"
BASE_URL_AUTHOR = "http://localhost:8080/api/authors"
BASE_URL_EDITOR = "http://localhost:8080/api/editors"
BASE_URL_TYPE = "http://localhost:8080/api/types"
BASE_URL_GENRE = "http://localhost:8080/api/genres"
BASE_URL_BOOK = "http://localhost:8080/api/books"
BASE_URL_REVIEW = "http://localhost:8080/api/reviews"

admin_auth = HTTPBasicAuth("admin", "admin")

def print_json(response):
    try:
        print(json.dumps(response.json(), indent=4, ensure_ascii=False))
    except:
        print("Pas de JSON :", response.text)

def safe_get(response, key):
    if response.status_code in [200, 201]:
        return response.json().get(key)
    return None

# ======================
# Dépendances
# ======================
def create_type(type_name):
    print("Création d'un Type :", type_name)
    payload = {"typeName": type_name}
    resp = requests.post(BASE_URL_TYPE, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "typeId")

def create_genre(genre_name):
    print("Création d'un Genre :", genre_name)
    payload = {"genreName": genre_name}
    resp = requests.post(BASE_URL_GENRE, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "genreId")

def create_author(first_name, last_name):
    print("Création d'un Auteur :", first_name, last_name)
    payload = {"authorFirstName": first_name, "authorLastName": last_name}
    resp = requests.post(BASE_URL_AUTHOR, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "authorId")

def create_editor(editor_name, siret, type_id):
    print("Création d'un Éditeur :", editor_name)
    payload = {
        "editorName": editor_name,
        "editorSIRET": siret,
        "types": [{"typeId": type_id}]
    }
    resp = requests.post(BASE_URL_EDITOR, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "editorId")

def create_book(title, pub_date, author_id, editor_id, type_id, genre_id):
    print("Création d'un Book :", title)
    payload = {
        "bookTitle": title,
        "bookPublicationDate": pub_date,
        "author": {"authorId": author_id},
        "editor": {"editorId": editor_id},
        "type": {"typeId": type_id},
        "genres": [{"genreId": genre_id}],
        "bookDescription": "Un livre fascinant",
        "numberOfPages": 300
    }
    resp = requests.post(BASE_URL_BOOK, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "isbn")

def create_review(user_id, book_id, rate, description):
    print("Création d'une Review")
    payload = {
        "reviewRate": rate,
        "reviewDescription": description,
        "user": {"userId": user_id},
        "book": {"isbn": book_id}
    }
    resp = requests.post(BASE_URL_REVIEW, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "reviewId")

# ======================
# Users
# ======================
def create_user(name, username, password, role):
    print("Création d'un Utilisateur :", username)
    payload = {
        "userName": name,
        "userUsername": username,
        "userPassword": password,
        "role": role
    }
    resp = requests.post(BASE_URL_USER, json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "userId")

def get_all_users():
    print("Récupération de tous les Users")
    resp = requests.get(BASE_URL_USER)  # GET user public ou protégé ?
    print("Status Code:", resp.status_code)
    print_json(resp)

def get_user_by_id(user_id):
    print(f"Récupération du User ID {user_id}")
    resp = requests.get(f"{BASE_URL_USER}/{user_id}")
    print("Status Code:", resp.status_code)
    print_json(resp)

def update_user(user_id, name, username, password, role, favorite_books):
    print(f"Mise à jour du User ID {user_id}")
    payload = {
        "userName": name,
        "userUsername": username,
        "userPassword": password,
        "role": role,
        "favoriteBooks": favorite_books
    }
    resp = requests.put(f"{BASE_URL_USER}/{user_id}", json=payload, auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)

def delete_user(user_id):
    print(f"Suppression du User ID {user_id}")
    resp = requests.delete(f"{BASE_URL_USER}/{user_id}", auth=admin_auth)
    print("Status Code:", resp.status_code)

def count_users():
    print("Comptage des Users")
    resp = requests.get(f"{BASE_URL_USER}/count")
    print("Status Code:", resp.status_code)
    print_json(resp)

def search_user_by_name(name):
    print(f"Recherche de Users par nom '{name}'")
    resp = requests.get(f"{BASE_URL_USER}/search/name", params={"name": name})
    print("Status Code:", resp.status_code)
    print_json(resp)

def search_user_by_username(username):
    print(f"Recherche de User par username '{username}'")
    resp = requests.get(f"{BASE_URL_USER}/search/username", params={"username": username})
    print("Status Code:", resp.status_code)
    print_json(resp)

def get_reviews_by_user(user_id):
    print(f"Récupération des Reviews du User ID {user_id}")
    resp = requests.get(f"{BASE_URL_USER}/{user_id}/reviews")
    print("Status Code:", resp.status_code)
    print_json(resp)

def get_books_reviewed_by_user(user_id):
    print(f"Récupération des Books reviewés par le User ID {user_id}")
    resp = requests.get(f"{BASE_URL_USER}/{user_id}/books-reviewed")
    print("Status Code:", resp.status_code)
    print_json(resp)

def add_book_to_favorites(user_id, book_id):
    print(f"Ajout du Book ID {book_id} aux favoris du User ID {user_id}")
    resp = requests.put(f"{BASE_URL_USER}/{user_id}/favorites/{book_id}", auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)

def remove_book_from_favorites(user_id, book_id):
    print(f"Suppression du Book ID {book_id} des favoris du User ID {user_id}")
    resp = requests.delete(f"{BASE_URL_USER}/{user_id}/favorites/{book_id}", auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)

def get_favorite_books(user_id):
    print(f"Récupération des favoris du User ID {user_id}")
    resp = requests.get(f"{BASE_URL_USER}/{user_id}/favorites", auth=admin_auth)
    print("Status Code:", resp.status_code)
    print_json(resp)


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

    # 1) Création d'un utilisateur
    user_id = create_user("Jean Valjean", "valjean", "password", "READER")
    if not user_id:
        print("Erreur lors de la création du user. Arrêt des tests.")
        return

    # 2) Ajouter une review associée à ce user et ce book
    review_id = create_review(user_id, book_id, 5, "Chef-d'œuvre intemporel.")
    if not review_id:
        print("Erreur lors de la création de la review.")

    # 3) Ajouter le livre en favori via update_user (optionnel)
    update_user(user_id, "Jean Valjean", "valjean_updated", "newpass", "READER", [{"isbn": book_id}])

    # 4) Ajouter le livre en favori avec endpoint dédié
    add_book_to_favorites(user_id, book_id)

    # 5) Récupérer la liste des favoris
    get_favorite_books(user_id)

    # 6) Supprimer ce favori
    remove_book_from_favorites(user_id, book_id)

    # 7) Vérifications globales
    get_all_users()
    get_user_by_id(user_id)
    search_user_by_name("Jean Valjean")
    search_user_by_username("valjean_updated")
    count_users()

    # 8) Reviews et livres reviewés
    get_reviews_by_user(user_id)
    get_books_reviewed_by_user(user_id)

    # 9) Suppression du user
    delete_user(user_id)
    get_user_by_id(user_id)  # Doit renvoyer 404 ou 401 selon la config

    print("==============================")
    print("FIN DES TESTS API USER")
    print("==============================")

if __name__ == "__main__":
    run_tests()
