SRCFILES := $(shell find src -name \*.java)
RESFILES := $(shell (find src -name \*.png ; find src -name \*.map ; find src -name \*.jnilib) | sed s/^src\\///)
PACKAGES := $(shell find src/com/kreative -type d \! -path \*/.\* | sed s/^src\\/// | tr / .)

all: clean bin doc jars apps exes kgb.tgz

eclipseall: eclipseclean jars apps exes kgb.tgz

clean:
	rm -rf bin
	rm -rf doc
	rm -rf *.jar
	rm -rf *.app
	rm -rf *.exe
	rm -rf kgb.tgz
	rm -rf dist

eclipseclean:
	rm -rf *.jar
	rm -rf *.app
	rm -rf *.exe
	rm -rf kgb.tgz
	rm -rf dist

jni-bin: jni-src
	rm -rf jni-bin
	mkdir -p jni-bin
	rm -f com_kreative_acc_pushchar_MacCharInFont.o
	cc -c -I/Library/Java/Home/include jni-src/com_kreative_acc_pushchar_MacCharInFont.c
	cc -dynamiclib -o jni-bin/libMacCharInFont.jnilib com_kreative_acc_pushchar_MacCharInFont.o -framework JavaVM -framework CoreFoundation -framework ApplicationServices
	rm -f com_kreative_acc_pushchar_MacCharInFont.o

bin: src
	mkdir -p bin
	javac -classpath dep/orange-extensions.jar -sourcepath src $(SRCFILES) -d bin
	$(foreach res,$(RESFILES),cp src/$(res) bin/$(res);)
	basename -a src/com/kreative/recode/maps/*.map > src/com/kreative/recode/maps/maps.index
	cp src/com/kreative/recode/maps/maps.index bin/com/kreative/recode/maps/

doc: src
	mkdir -p doc
	javadoc -classpath dep/orange-extensions.jar -sourcepath src $(PACKAGES) -d doc

jars: charsetskk.jar kte.jar livefilesize.jar screenruler.jar pushchar.jar javaprops.jar recode.jar ponyfix.jar
	chmod a+x *.jar

charsetskk.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	rm -rf dist
	mkdir -p dist/META-INF/services
	cp dep/java.nio.charset.spi.CharsetProvider dist/META-INF/services/
	jar cmf dep/MANIFEST-CHARSET.MF charsetskk.jar -C dist META-INF -C bin com/kreative/charset
	rm -rf dist

kte.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-KTE.MF kte.jar -C bin com/kreative/kte

livefilesize.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-LFS.MF livefilesize.jar -C bin com/kreative/acc/livefilesize

screenruler.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-SR.MF screenruler.jar -C bin com/kreative/acc/screenruler

pushchar.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-PCH.MF pushchar.jar -C bin com/kreative/acc/pushchar -C bin com/kreative/acc/shared/unidata -C bin com/kreative/awt

javaprops.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-JP.MF javaprops.jar -C bin com/kreative/acc/javaprops

recode.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-RECODE.MF recode.jar -C bin com/kreative/recode

ponyfix.jar: bin
	export COPYFILE_DISABLE=true
	find bin -name .DS_Store -delete
	jar cmf dep/MANIFEST-PFIX.MF ponyfix.jar -C bin com/kreative/ponyfix

apps: Recode.app

Recode.app: dep recode.jar
	mkdir -p Recode.app/Contents/MacOS
	mkdir -p Recode.app/Contents/Resources/Java
	cp -f dep/PkgInfo-Recode Recode.app/Contents/PkgInfo
	cp -f dep/Info-Recode.plist Recode.app/Contents/Info.plist
	cp -f dep/universalJavaApplicationStub Recode.app/Contents/MacOS/Recode
	cp -f dep/Recode.icns Recode.app/Contents/Resources
	cp -f recode.jar Recode.app/Contents/Resources/Java

exes: recode.exe

recode.exe: dep recode.jar
ifeq ($(LAUNCH4J_HOME),)
	echo 'Please set $$LAUNCH4J_HOME environment variable to compile exe.'
else
	cd "$(LAUNCH4J_HOME)" ; java -Dlaunch4j.tmpdir=/tmp/ -jar "$(LAUNCH4J_HOME)/launch4j.jar" "$(PWD)/dep/Recode.xml"
endif

kgb.tgz: jars
	tar -czf kgb.tgz *.jar

.PHONY: all eclipseall clean eclipseclean jars apps exes
