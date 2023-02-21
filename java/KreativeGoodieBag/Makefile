SRCFILES := $(shell find src -name \*.java)
RESFILES := $(shell find src -type f \! -name .\* \! -name \*.java | sed s/^src\\///)
PACKAGES := $(shell find src -name \*.java | sed -E s/^src\\/\|\\/[^\\/]+$$//g | grep -v \\.java$$ | sort | uniq | tr / .)

all: clean bin doc jars

eclipseall: eclipseclean jars

clean:
	rm -rf bin
	rm -rf doc
	rm -rf *.jar

eclipseclean:
	rm -rf *.jar

bin: src
	mkdir -p bin
ifeq ($(origin JAVACFLAGS), undefined)
	$(eval JAVACFLAGS = $(shell dep/minJavaVerOpts))
endif
	javac $(JAVACFLAGS) -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)

doc: src
	mkdir -p doc
	javadoc $(JAVADOCFLAGS) -sourcepath src $(PACKAGES) -d doc

jars: kte.jar livefilesize.jar screenruler.jar javaprops.jar ponyfix.jar

kte.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-KTE.MF kte.jar -C bin com/kreative/kte
	chmod +x kte.jar

livefilesize.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-LFS.MF livefilesize.jar -C bin com/kreative/acc/livefilesize
	chmod +x livefilesize.jar

screenruler.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-SR.MF screenruler.jar -C bin com/kreative/acc/screenruler
	chmod +x screenruler.jar

javaprops.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-JP.MF javaprops.jar -C bin com/kreative/acc/javaprops
	chmod +x javaprops.jar

ponyfix.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-PFIX.MF ponyfix.jar -C bin com/kreative/ponyfix
	chmod +x ponyfix.jar

.PHONY: all eclipseall clean eclipseclean jars
