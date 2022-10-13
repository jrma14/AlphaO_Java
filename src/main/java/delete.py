import os


def main():
    print('deleting')
    try:
        os.remove('AlphaX.go')
    except:
        pass
    try:
        os.remove('AlphaO.go')
    except:
        pass
    try:
        os.remove('move_file')
    except:
        pass
    try:
        os.remove('first_four_moves')
    except:
        pass
    try:
        os.remove('end_game')
    except:
        pass
