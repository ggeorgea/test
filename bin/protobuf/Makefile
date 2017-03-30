PROTO_FILES=$(shell find src/ -name '*.proto')
rwildcard=$(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2) $(filter $(subst *,%,$2),$d))
PROTOS=$(call rwildcard, , *.proto)

JAR_PROTOBUF="http://central.maven.org/maven2/com/google/protobuf/protobuf-java/3.1.0/protobuf-java-3.1.0.jar"
PROTO_ZIP_PATH="https://github.com/google/protobuf/releases/download/v3.1.0/protoc-3.1.0-linux-x86_64.zip"

SRC=src
OUT=build

PROTO_ZIP=$(OUT)/protoc-binary.zip
PROTOC_PATH=$(OUT)/proto-compiler
GO_OUT=$(OUT)/go
JAVA_OUT=$(OUT)/java
JAR_OUT=$(OUT)/catan-protocol.jar
CPP_OUT=$(OUT)/cpp
CS_OUT=$(OUT)/csharp
JS_OUT=$(OUT)/js

PATH := $(PROTOC_PATH)/bin:$(PATH);

all: install

.PHONY: install

install:
	mkdir -p $(PROTOC_PATH)
	if ! protoc --version | grep 3\.1\.0 > /dev/null; then \
		wget -O $(PROTO_ZIP) $(PROTO_ZIP_PATH) && \
		unzip -o $(PROTO_ZIP) -d $(PROTOC_PATH) && \
		echo "Downloaded protobuf"; \
	fi

go: install
	mkdir -p $(GO_OUT)
	protowrap -I $(SRC) $(PROTO_FILES) --go_out $(GO_OUT)

java: install
	mkdir -p $(JAVA_OUT)
	protoc -I $(SRC) $(PROTO_FILES) --java_out $(JAVA_OUT)

jar: java
	wget -O $(JAR_OUT) $(JAR_PROTOBUF)
	javac -cp $(JAR_OUT) $(shell find build/ -name "*.java") -d $(JAVA_OUT) 
	jar uf $(JAR_OUT) -C $(JAVA_OUT) .

cpp: install
	mkdir -p $(CPP_OUT)
	protoc -I $(SRC) $(PROTO_FILES) --cpp_out $(CPP_OUT) 

csharp: install
	mkdir -p $(CS_OUT)
	protoc -I $(SRC) --csharp_out $(CS_OUT) $(PROTOS)

javascript: install
	mkdir -p $(JS_OUT)
	protoc -I $(SRC) --js_out=library=$(JS_OUT)/proto_lib,binary:. $(PROTO_FILES)

clean:
	rm -rf $(OUT) 
