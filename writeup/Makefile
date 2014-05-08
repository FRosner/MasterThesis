DOKUMENT = main

all: $(DOKUMENT).pdf

$(DOKUMENT).pdf: $(DOKUMENT).tex
	./dia2tex.sh
	pdflatex $(DOKUMENT).tex > /dev/null
	bibtex $(DOKUMENT) > /dev/null
	pdflatex $(DOKUMENT).tex > /dev/null
	pdflatex $(DOKUMENT).tex > /dev/null