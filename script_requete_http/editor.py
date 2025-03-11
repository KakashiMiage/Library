import requests
import json

# URLs de base
BASE_URL_EDITOR = "http://localhost:8080/api/editors"
BASE_URL_TYPE = "http://localhost:8080/api/types"
BASE_URL_BOOK = "http://localhost:8080/api/books"
BASE_URL_AUTHOR = "http://localhost:8080/api/authors"
BASE_URL_GENRE = "http://localhost:8080/api/genres"

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
    except Exception as e:
        print(f"Erreur de parsing JSON: {e}")
        print(response.text)

# ====================
# CRUD sur Types & Books pour dépendances
# ====================
def create_type(type_name):
    print("Création d'un type")
    payload = {"typeName": type_name}
    response = requests.post(BASE_URL_TYPE, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("typeId") if response.status_code == 201 else None

def create_genre(genre_name):
    print("Création d'un genre")
    payload = {"genreName": genre_name}
    response = requests.post(BASE_URL_GENRE, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("genreId") if response.status_code == 201 else None

def create_author(first_name, last_name):
    print("Création d'un auteur pour un livre")
    payload = {
        "authorFirstName": first_name,
        "authorLastName": last_name
    }
    response = requests.post(BASE_URL_AUTHOR, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("authorId") if response.status_code == 201 else None

def create_book(title, date, description, pages, author_id, editor_id, type_id, genre_ids):
    print("Création d'un livre pour un éditeur")
    payload = {
        "bookTitle": title,
        "bookPublicationDate": date,
        "bookDescription": description,
        "numberOfPages": pages,
        "author": {"authorId": author_id},
        "editor": {"editorId": editor_id},
        "type": {"typeId": type_id},
        "genres": [{"genreId": gid} for gid in genre_ids]
    }
    response = requests.post(BASE_URL_BOOK, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("isbn") if response.status_code == 201 else None

# ====================
# CRUD sur Editors
# ====================
def create_editor(name, siret, type_ids):
    print("Création d'un éditeur")
    payload = {
        "editorName": name,
        "editorSIRET": siret,
        "types": [{"typeId": tid} for tid in type_ids]
    }
    response = requests.post(BASE_URL_EDITOR, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("editorId") if response.status_code == 201 else None

def get_editor_by_id(editor_id):
    print(f"Récupération de l'éditeur ID {editor_id}")
    response = requests.get(f"{BASE_URL_EDITOR}/{editor_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def update_editor(editor_id, name, siret, type_ids):
    print(f"Mise à jour de l'éditeur ID {editor_id}")
    payload = {
        "editorName": name,
        "editorSIRET": siret,
        "types": [{"typeId": tid} for tid in type_ids]
    }
    response = requests.put(f"{BASE_URL_EDITOR}/{editor_id}", json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def delete_editor(editor_id):
    print(f"Suppression de l'éditeur ID {editor_id}")
    response = requests.delete(f"{BASE_URL_EDITOR}/{editor_id}")
    print(f"Status Code: {response.status_code}")

def get_all_editors():
    print("Récupération de tous les éditeurs")
    response = requests.get(BASE_URL_EDITOR)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def count_editors():
    print("Compter les éditeurs")
    response = requests.get(f"{BASE_URL_EDITOR}/count")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_editor_by_name(name):
    print(f"Recherche de l'éditeur par nom '{name}'")
    response = requests.get(f"{BASE_URL_EDITOR}/search/name", params={"name": name})
    print(f"Status Code: {response.status_code}")
    print_json(response)

def search_editors(keyword):
    print(f"Recherche d'éditeurs par keyword '{keyword}'")
    response = requests.get(f"{BASE_URL_EDITOR}/search", params={"keyword": keyword})
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_editors_by_type(type_id):
    print(f"Recherche d'éditeurs par Type ID {type_id}")
    response = requests.get(f"{BASE_URL_EDITOR}/search/type/{type_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_books_by_editor(editor_id):
    print(f"Récupération des livres édités par l'éditeur ID {editor_id}")
    response = requests.get(f"{BASE_URL_EDITOR}/{editor_id}/books")
    print(f"Status Code: {response.status_code}")
    print_json(response)

# ====================
# Lancement des tests
# ====================
def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API EDITOR")
    print("==============================")

    # Créer une dépendance Type
    type_id = create_type("Essai")
    if not type_id:
        print("Échec de la création du Type. Arrêt des tests.")
        return

    # Créer un éditeur
    editor_id = create_editor("Larousse", 12345678901234, [type_id])
    if not editor_id:
        print("Échec de la création de l'éditeur. Arrêt des tests.")
        return

    # Création d'un livre pour l'éditeur
    genre_id = create_genre("Science")
    author_id = create_author("René", "Descartes")
    book_id = create_book(
        "Discours de la méthode",
        "1637-01-01",
        "Ouvrage philosophique majeur.",
        200,
        author_id,
        editor_id,
        type_id,
        [genre_id]
    )

    # Vérifications sur l'éditeur
    get_all_editors()
    get_editor_by_id(editor_id)

    # Update de l'éditeur
    update_editor(editor_id, "Larousse - Édition Révisée", 98765432101234, [type_id])

    # Recherche
    get_editor_by_name("Larousse - Édition Révisée")
    search_editors("Larousse")
    get_editors_by_type(type_id)

    # Récupérer les livres de cet éditeur
    get_books_by_editor(editor_id)

    # Compter les éditeurs
    count_editors()

    # Suppression de l'éditeur
    delete_editor(editor_id)

    # Vérifier la suppression
    get_editor_by_id(editor_id)

    print("==============================")
    print("FIN DES TESTS API EDITOR")
    print("==============================")

if __name__ == "__main__":
    run_tests()
