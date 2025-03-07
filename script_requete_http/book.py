import requests
import json

BASE_URL = "http://localhost:8080/books"  # Remplace par l'URL réelle de ton API

def create_book(book_data):
    """Créer un nouveau livre."""
    response = requests.post(f"{BASE_URL}", json=book_data)
    return response.json()

def find_all_books():
    """Récupérer tous les livres."""
    response = requests.get(f"{BASE_URL}")
    return response.json()

def get_book_by_id(book_id):
    """Récupérer un livre par son ID."""
    response = requests.get(f"{BASE_URL}/{book_id}")
    return response.json()

def update_book(book_id, updated_data):
    """Mettre à jour un livre."""
    response = requests.put(f"{BASE_URL}/{book_id}", json=updated_data)
    return response.json()

def delete_book(book_id):
    """Supprimer un livre."""
    response = requests.delete(f"{BASE_URL}/{book_id}")
    return response.status_code

def find_books_by_title(title):
    """Rechercher des livres par titre."""
    response = requests.get(f"{BASE_URL}/title/{title}")
    return response.json()

def count_books():
    """Compter le nombre total de livres."""
    response = requests.get(f"{BASE_URL}/count")
    return response.json()

def get_books_by_author(author_id):
    """Récupérer les livres d'un auteur spécifique."""
    response = requests.get(f"{BASE_URL}/author/{author_id}")
    return response.json()

def get_books_by_genre(genre_id):
    """Récupérer les livres d'un genre spécifique."""
    response = requests.get(f"{BASE_URL}/genre/{genre_id}")
    return response.json()

def get_books_by_editor(editor_id):
    """Récupérer les livres d'un éditeur spécifique."""
    response = requests.get(f"{BASE_URL}/editor/{editor_id}")
    return response.json()

def get_books_by_type(type_id):
    """Récupérer les livres d'un type spécifique."""
    response = requests.get(f"{BASE_URL}/type/{type_id}")
    return response.json()

def search_books(keyword):
    """Rechercher des livres par mot-clé."""
    response = requests.get(f"{BASE_URL}/search", params={"keyword": keyword})
    return response.json()

def get_books_with_reviews():
    """Récupérer les livres ayant des avis."""
    response = requests.get(f"{BASE_URL}/reviews")
    return response.json()

def get_top_rated_books(min_rating):
    """Récupérer les livres ayant une note minimale."""
    response = requests.get(f"{BASE_URL}/top-rated", params={"minRating": min_rating})
    return response.json()

# Test des fonctions
if __name__ == "__main__":
    # # Création d'un livre de test
    # test_book = {
    #     "bookTitle": "L'Étranger",
    #     "bookPublicationDate": "1942-06-01",
    #     "editor": {
    #         "editorId": 1,
    #         "editorName": "Gabriel",
    #         "editorSIRET": 1,
    #         "typeId": 1
    #     },
    #     "author": {
    #         "authorId": 1,
    #         "authorFirstName": "Gabriel",
    #         "authorLastName": "LOPES NEVES"
    #     },
    #     "type": {
    #         "typeId": 1,
    #         "typeName": "Manga"
    #     },
    #     "genre": {
    #         "genreId": 1,
    #         "genreName": "Aventure"
    #     },
    #     "bookDescription": "Un classique de la littérature.",
    #     "numberOfPages": 123
    # }
    # created_book = create_book(test_book)
    # print("Livre créé:", json.dumps(created_book, indent=2))

    # # Récupérer tous les livres
    # books = find_all_books()
    # print("Liste des livres:", json.dumps(books, indent=2))

    # # Récupérer un livre par ID
    # books = find_all_books()
    # if books:
    #     book_id = books[0]["isbn"]
    #     book = get_book_by_id(book_id)
    #     print("Livre trouvé par ID:", book)

    # # Mettre à jour un livre
    # updated_data = {
    #     "bookTitle": "L'Étranger - Nouvelle Édition",
    #     "bookPublicationDate": "1942-06-01",
    #     "editor": {
    #         "editorId": 1
    #     },
    #     "author": {
    #         "authorId": 1
    #     },
    #     "type": {
    #         "typeId": 1
    #     },
    #     "genre": {
    #         "genreId": 1
    #     },
    #     "bookDescription": "Un chef-d'œuvre intemporel.",
    #     "numberOfPages": 130
    # }
    # updated_book = update_book(book_id, updated_data)
    # print("Livre mis à jour:", json.dumps(updated_book, indent=2))

    # # Recherche par titre
    # search_results = find_books_by_title("Étranger")
    # print("Livres trouvés par titre:", json.dumps(search_results, indent=2))

    # # Nombre total de livres
    # total_books = count_books()
    # print("Nombre total de livres:", json.dumps(total_books, indent=2))

    # # Récupérer les livres par auteur
    # author_books = get_books_by_author(1)
    # print("Livres de l'auteur 1:", json.dumps(author_books, indent=2))

    # # Récupérer les livres par genre
    # genre_books = get_books_by_genre(1)
    # print("Livres du genre 1:", json.dumps(genre_books, indent=2))

    # # Récupérer les livres par éditeur
    # editor_books = get_books_by_editor(1)
    # print("Livres de l'éditeur 1:", json.dumps(editor_books, indent=2))

    # # Récupérer les livres par type
    # type_books = get_books_by_type(1)
    # print("Livres du type 1:", json.dumps(type_books, indent=2))

    # # Recherche par mot-clé
    # keyword_books = search_books("Étrange")
    # print("Livres trouvés par mot-clé:", json.dumps(keyword_books, indent=2))

    # # Récupérer les livres avec des avis
    # books_with_reviews = get_books_with_reviews()
    # print("Livres avec avis:", books_with_reviews)

    # # Récupérer les livres les mieux notés
    # top_books = get_top_rated_books(4)
    # print("Livres avec une note >= 4:", top_books)

    # Suppression du livre
    delete_status = delete_book(6)
    print(f"Livre supprimé, statut HTTP: {delete_status}")
