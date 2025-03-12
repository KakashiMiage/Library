import requests
import json
from requests.auth import HTTPBasicAuth

# ======================
# Configuration de base
# ======================
BASE_URL = "http://localhost:8080/api/reviews"
admin_auth = HTTPBasicAuth("admin", "admin")

def print_json(resp):
    try:
        print(json.dumps(resp.json(), indent=4, ensure_ascii=False))
    except:
        print("Pas de JSON. Réponse brute :")
        print(resp.text)

def safe_get(resp, key):
    if resp.status_code in [200, 201]:
        return resp.json().get(key)
    return None

# ===================================
# Fonctions utilitaires (dépendances)
# ===================================

def create_type(name):
    url = "http://localhost:8080/api/types"
    payload = {"typeName": name}
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("Création d'un type :", name)
    print(f"Status: {resp.status_code}")
    print_json(resp)
    return safe_get(resp, "typeId")

def create_genre(name):
    url = "http://localhost:8080/api/genres"
    payload = {"genreName": name}
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("Création d'un genre :", name)
    print(f"Status: {resp.status_code}")
    print_json(resp)
    return safe_get(resp, "genreId")

def create_author(first_name, last_name):
    url = "http://localhost:8080/api/authors"
    payload = {
        "authorFirstName": first_name,
        "authorLastName": last_name
    }
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("Création d'un auteur :", first_name, last_name)
    print(f"Status: {resp.status_code}")
    print_json(resp)
    return safe_get(resp, "authorId")

def create_editor(name, siret, type_id):
    url = "http://localhost:8080/api/editors"
    payload = {
        "editorName": name,
        "editorSIRET": siret,
        "types": [{"typeId": type_id}]
    }
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("Création d'un éditeur :", name)
    print(f"Status: {resp.status_code}")
    print_json(resp)
    return safe_get(resp, "editorId")

def create_user(name, username, password, role):
    url = "http://localhost:8080/api/users"
    payload = {
        "userName": name,
        "userUsername": username,
        "userPassword": password,
        "role": role  # "READER" ou "ADMIN"
    }
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("Création d'un utilisateur :", username)
    print(f"Status: {resp.status_code}")
    print_json(resp)
    return safe_get(resp, "userId")

def create_book(title, date, description, pages, author_id, editor_id, type_id, genre_id):
    url = "http://localhost:8080/api/books"
    payload = {
        "bookTitle": title,
        "bookPublicationDate": date,
        "bookDescription": description,
        "numberOfPages": pages,
        "author": {"authorId": author_id},
        "editor": {"editorId": editor_id},
        "type": {"typeId": type_id},
        "genres": [{"genreId": genre_id}]
    }
    resp = requests.post(url, json=payload, auth=admin_auth)
    print("Création d'un livre :", title)
    print(f"Status: {resp.status_code}")
    print_json(resp)
    return safe_get(resp, "isbn")

# ===================================
# Fonctions CRUD Reviews
# ===================================

def create_review(rate, description, user_id, book_id):
    print("Création d'une review")
    payload = {
        "reviewRate": rate,
        "reviewDescription": description,
        "user": {"userId": user_id},
        "book": {"isbn": book_id}
    }
    resp = requests.post(BASE_URL, json=payload, auth=admin_auth)
    print("Status:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "reviewId")

def get_all_reviews():
    print("Récupération de toutes les reviews")
    resp = requests.get(BASE_URL)  # Lecture publique si config le permet
    print("Status:", resp.status_code)
    print_json(resp)

def get_review_by_id(review_id):
    print(f"Récupération de la review ID {review_id}")
    resp = requests.get(f"{BASE_URL}/{review_id}")
    print("Status:", resp.status_code)
    print_json(resp)

def update_review(review_id, new_rate, new_description, user_id, book_id):
    print(f"Mise à jour de la review ID {review_id}")
    payload = {
        "reviewRate": new_rate,
        "reviewDescription": new_description,
        "user": {"userId": user_id},
        "book": {"isbn": book_id}
    }
    resp = requests.put(f"{BASE_URL}/{review_id}", json=payload, auth=admin_auth)
    print("Status:", resp.status_code)
    print_json(resp)

def delete_review(review_id):
    print(f"Suppression de la review ID {review_id}")
    resp = requests.delete(f"{BASE_URL}/{review_id}", auth=admin_auth)
    print("Status:", resp.status_code)
    print(resp.text)

def count_reviews():
    print("Nombre total de reviews")
    resp = requests.get(f"{BASE_URL}/count")
    print("Status:", resp.status_code)
    print_json(resp)

def get_reviews_by_book(book_id):
    print(f"Récupération des reviews du book ID {book_id}")
    resp = requests.get(f"{BASE_URL}/book/{book_id}")
    print("Status:", resp.status_code)
    print_json(resp)

def get_reviews_by_user(user_id):
    print(f"Récupération des reviews du user ID {user_id}")
    resp = requests.get(f"{BASE_URL}/user/{user_id}")
    print("Status:", resp.status_code)
    print_json(resp)

def get_average_rating(book_id):
    print(f"Récupération de la moyenne des reviews pour le book ID {book_id}")
    resp = requests.get(f"{BASE_URL}/book/{book_id}/average-rating")
    print("Status:", resp.status_code)
    print_json(resp)

def get_top_rated_books(min_rating):
    print(f"Récupération des books avec une note >= {min_rating}")
    resp = requests.get(f"{BASE_URL}/top-rated", params={"minRating": min_rating})
    print("Status:", resp.status_code)
    print_json(resp)

# ===================================
# TEST RUNNER
# ===================================

def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API REVIEW")
    print("==============================")

    # Création des dépendances (avec authentification admin)
    type_id = create_type("Philosophie")
    genre_id = create_genre("Essai")
    author_id = create_author("Platon", "Athénien")
    editor_id = create_editor("PhiloPress", 99999999999999, type_id)
    user_id = create_user("Socrate", "socrate123", "password", "READER")

    book_id = create_book(
        "La République",
        "2000-01-01",
        "Dialogue sur la justice.",
        350,
        author_id,
        editor_id,
        type_id,
        genre_id
    )

    # Vérifier que tout est bien créé
    if not all([type_id, genre_id, author_id, editor_id, user_id, book_id]):
        print("Erreur lors de la création des dépendances. Arrêt des tests.")
        return

    # 1) Création d'une review
    review_id = create_review(5, "Excellent ouvrage !", user_id, book_id)
    if not review_id:
        print("Erreur lors de la création de la review. Arrêt des tests.")
        return

    # 2) Toutes les reviews
    get_all_reviews()

    # 3) Récupérer review par ID
    get_review_by_id(review_id)

    # 4) Mise à jour de la review
    update_review(review_id, 4, "Réflexion intéressante mais dense.", user_id, book_id)

    # 5) Récupérer toutes les reviews de ce livre
    get_reviews_by_book(book_id)

    # 6) Récupérer toutes les reviews de cet utilisateur
    get_reviews_by_user(user_id)

    # 7) Nombre total de reviews
    count_reviews()

    # 8) Moyenne des notes du livre
    get_average_rating(book_id)

    # 9) Livres top-rated >= 4
    get_top_rated_books(4)

    # 10) Suppression de la review
    delete_review(review_id)

    # 11) Vérifier la suppression (devrait renvoyer 404)
    get_review_by_id(review_id)

    print("==============================")
    print("FIN DES TESTS COMPLETS API REVIEW")
    print("==============================")

if __name__ == "__main__":
    run_tests()
