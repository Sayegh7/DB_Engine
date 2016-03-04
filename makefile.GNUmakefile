JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
		BTree.java\
        DBApp.java\
        DBAppTest.java \
        InnerNode.java\
        Node.java\
        LeafNode.java\
        Key.java\
        HardDrive.java \
        Page.java \
		Reflector.java \
		SearchResult.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class