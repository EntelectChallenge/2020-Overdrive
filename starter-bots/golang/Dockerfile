
FROM entelectchallenge/base:2019

# gcc for cgo
RUN apt-get update && apt-get install -y --no-install-recommends \
		g++ \
		gcc \
		libc6-dev \
		make \
		pkg-config \
        wget \
        git \
	&& rm -rf /var/lib/apt/lists/*

ENV GOLANG_VERSION 1.14.2
ENV TENSORFLOW_VERSION 1.15.0

RUN set -eux; \
	goSha256='6272d6e940ecb71ea5636ddb5fab3933e087c1356173c61f4a803895e947ebb3'; \
	url="https://golang.org/dl/go${GOLANG_VERSION}.linux-amd64.tar.gz"; \
	wget --progress=bar:force:noscroll -O go.tgz "$url"; \
	echo "${goSha256} *go.tgz" | sha256sum -c -; \
	tar -C /usr/local -xzf go.tgz; \
	rm go.tgz; \
    export PATH="/usr/local/go/bin:$PATH"; \
	go version; \
    wget --progress=bar:force:noscroll -O tensorflow.tgz "https://storage.googleapis.com/tensorflow/libtensorflow/libtensorflow-cpu-linux-x86_64-${TENSORFLOW_VERSION}.tar.gz" ; \
    tar -C /usr/local -xzf tensorflow.tgz; \
    rm tensorflow.tgz; \
    ldconfig;

ENV GOPATH /go
ENV PATH $GOPATH/bin:/usr/local/go/bin:$PATH

RUN mkdir -p "$GOPATH/src" "$GOPATH/bin" && chmod -R 777 "$GOPATH"

COPY . /app
WORKDIR /app
RUN go build ./... ; go build .;