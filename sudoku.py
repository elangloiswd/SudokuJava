import random

def generer_grille_sudoku():
  """Génère une grille de Sudoku aléatoire au format spécifié.

  Returns:
    str: Une chaîne de caractères représentant la grille.
  """

  grille = ""
  for ligne in range(8):
    for colonne in range(8):
      valeur = random.randint(1, 9)
      grille += f"{ligne:02d}{colonne:02d}{valeur} "
  return grille

# Générer et afficher une grille
grille_aleatoire = generer_grille_sudoku()
print(grille_aleatoire)