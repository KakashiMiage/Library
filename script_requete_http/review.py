import requests
import json

BASE_URL = "http://localhost:8080/api/reviews"

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
    except Exception as e:
        print(f"Erreur de parsing JSON: {e}")
        print(response.text)

# ===================================
# Fonctions utilitaires pour les dépendances
# ===================================

def create_type(name):
    url = "http://localhost:8080/api/types"
    payload = {"typeName": name}
    response = requests.post(url, json=payload)
    print("Création d'un type")
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("typeId")

def create_genre(name):
    url = "http://localhost:8080/api/genres"
    payload = {"genreName": name}
    response = requests.post(url, json=payload)
    print("Création d'un genre")
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("genreId")

def create_author(first_name, last_name):
    url = "http://localhost:8080/api/authors"
    payload = {
        "authorFirstName": first_name,
        "authorLastName": last_name
    }
    response = requests.post(url, json=payload)
    print("Création d'un auteur")
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("authorId")

def create_editor(name, siret, type_id):
    url = "http://localhost:8080/api/editors"
    payload = {
        "editorName": name,
        "editorSIRET": siret,
        "types": [{"typeId": type_id}]
    }
    response = requests.post(url, json=payload)
    print("Création d'un éditeur")
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("editorId")

def create_user(name, username, password, role):
    url = "http://localhost:8080/api/users"
    payload = {
        "userName": name,
        "userUsername": username,
        "userPassword": password,
        "role": role  # Doit être "READER" ou "ADMIN"
    }
    response = requests.post(url, json=payload)
    print("Création d'un utilisateur")
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("userId")

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
    response = requests.post(url, json=payload)
    print("Création d'un livre")
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("isbn")

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
    response = requests.post(BASE_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("reviewId") if response.status_code == 201 else None

def get_all_reviews():
    print("Récupération de toutes les reviews")
    response = requests.get(BASE_URL)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_review_by_id(review_id):
    print(f"Récupération de la review ID {review_id}")
    response = requests.get(f"{BASE_URL}/{review_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def update_review(review_id, new_rate, new_description, user_id, book_id):
    print(f"Mise à jour de la review ID {review_id}")
    payload = {
        "reviewRate": new_rate,
        "reviewDescription": new_description,
        "user": {"userId": user_id},
        "book": {"isbn": book_id}
    }
    response = requests.put(f"{BASE_URL}/{review_id}", json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def delete_review(review_id):
    print(f"Suppression de la review ID {review_id}")
    response = requests.delete(f"{BASE_URL}/{review_id}")
    print(f"Status Code: {response.status_code}")

def count_reviews():
    print("Nombre total de reviews")
    response = requests.get(f"{BASE_URL}/count")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_reviews_by_book(book_id):
    print(f"Récupération des reviews du book ID {book_id}")
    response = requests.get(f"{BASE_URL}/book/{book_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_reviews_by_user(user_id):
    print(f"Récupération des reviews du user ID {user_id}")
    response = requests.get(f"{BASE_URL}/user/{user_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_average_rating(book_id):
    print(f"Récupération de la moyenne des reviews pour le book ID {book_id}")
    response = requests.get(f"{BASE_URL}/book/{book_id}/average-rating")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_top_rated_books(min_rating):
    print(f"Récupération des books avec une note supérieure ou égale à {min_rating}")
    response = requests.get(f"{BASE_URL}/top-rated", params={"minRating": min_rating})
    print(f"Status Code: {response.status_code}")
    print_json(response)

# ===================================
# TEST RUNNER
# ===================================

def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API REVIEW")
    print("==============================")

    # Création des dépendances
    type_id = create_type("Philosophie")
    genre_id = create_genre("Essai")
    author_id = create_author("Platon", "Athénien")
    editor_id = create_editor("PhiloPress", 99999999999999, type_id)
    user_id = create_user("Socrate", "socrate123", "password", "READER")
    book_id = create_book("La République", "2000-01-01", "Dialogue sur la justice.", 350, author_id, editor_id, type_id, genre_id)

    if not all([type_id, genre_id, author_id, editor_id, user_id, book_id]):
        print("Erreur lors de la création des dépendances. Arrêt des tests.")
        return

    # Création d'une review
    review_id = create_review(5, "Excellent ouvrage !", user_id, book_id)
    if not review_id:
        print("Erreur lors de la création de la review. Arrêt des tests.")
        return

    # Toutes les reviews
    get_all_reviews()

    # Récupérer par ID
    get_review_by_id(review_id)

    # Mise à jour de la review
    update_review(review_id, 4, "Réflexion intéressante mais dense.", user_id, book_id)

    # Reviews par book
    get_reviews_by_book(book_id)

    # Reviews par user
    get_reviews_by_user(user_id)

    # Compter les reviews
    count_reviews()

    # Moyenne des ratings du book
    get_average_rating(book_id)

    # Top rated books avec note >= 4
    get_top_rated_books(4)

    # Suppression de la review
    delete_review(review_id)

    # Vérifier la suppression
    get_review_by_id(review_id)

    print("==============================")
    print("FIN DES TESTS COMPLETS API REVIEW")
    print("==============================")

if __name__ == "__main__":
    run_tests()
