DOKUMENT = main

all:
	./dia2tex.sh
	pdflatex $(DOKUMENT).tex > /dev/null
	bibtex $(DOKUMENT) > /dev/null
	pdflatex $(DOKUMENT).tex > /dev/null
	pdflatex $(DOKUMENT).tex > /dev/null