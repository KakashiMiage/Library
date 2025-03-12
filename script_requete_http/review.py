import requests
import json
from requests.auth import HTTPBasicAuth

# ======================
# Configuration de base
# ======================
BASE_URL_REVIEWS = "http://localhost:8080/api/reviews"
BASE_URL = "http://localhost:8080/api"
admin_auth = HTTPBasicAuth("admin", "admin")

headers = {
    "Content-Type": "application/json"
}

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

def create_book(title, date, description, pages, author_id, editor_id, type_id, genre_id):
    url = BASE_URL+"/books"
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
    resp = requests.post(BASE_URL_REVIEWS, json=payload, auth=admin_auth)
    print("Status:", resp.status_code)
    print_json(resp)
    return safe_get(resp, "reviewId")

def get_all_reviews():
    print("Récupération de toutes les reviews")
    resp = requests.get(BASE_URL_REVIEWS)  # Lecture publique si config le permet
    print("Status:", resp.status_code)
    print_json(resp)

def get_review_by_id(review_id):
    print(f"Récupération de la review ID {review_id}")
    resp = requests.get(f"{BASE_URL_REVIEWS}/{review_id}")
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
    resp = requests.put(f"{BASE_URL_REVIEWS}/{review_id}", json=payload, auth=admin_auth)
    print("Status:", resp.status_code)
    print_json(resp)

def delete_review(review_id):
    print(f"Suppression de la review ID {review_id}")
    resp = requests.delete(f"{BASE_URL_REVIEWS}/{review_id}", auth=admin_auth)
    print("Status:", resp.status_code)
    print(resp.text)

def count_reviews():
    print("Nombre total de reviews")
    resp = requests.get(f"{BASE_URL_REVIEWS}/count")
    print("Status:", resp.status_code)
    print_json(resp)

def get_reviews_by_book(book_id):
    print(f"Récupération des reviews du book ID {book_id}")
    resp = requests.get(f"{BASE_URL_REVIEWS}/book/{book_id}")
    print("Status:", resp.status_code)
    print_json(resp)

def get_reviews_by_user(user_id):
    print(f"Récupération des reviews du user ID {user_id}")
    resp = requests.get(f"{BASE_URL_REVIEWS}/user/{user_id}")
    print("Status:", resp.status_code)
    print_json(resp)

def get_average_rating(book_id):
    print(f"Récupération de la moyenne des reviews pour le book ID {book_id}")
    resp = requests.get(f"{BASE_URL_REVIEWS}/book/{book_id}/average-rating")
    print("Status:", resp.status_code)
    print_json(resp)

def get_top_rated_books(min_rating):
    print(f"Récupération des books avec une note >= {min_rating}")
    resp = requests.get(f"{BASE_URL_REVIEWS}/top-rated", params={"minRating": min_rating})
    print("Status:", resp.status_code)
    print_json(resp)

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

# ===================================
# TEST RUNNER
# ===================================

def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API REVIEW")
    print("==============================")

    # Création des dépendances (avec authentification admin)
    type_id = create_type_if_not_exists("Philosophie")
    genre_id = create_genre_if_not_exists("Essai")
    author_id = create_author("Platon", "Athénien")
    editor_id = create_editor_if_not_exists("PhiloPress", 99999999999999, type_id)
    user_id = create_user_if_not_exists("Socrate", "socrate123", "password", "READER")

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
