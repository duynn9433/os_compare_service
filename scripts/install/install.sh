#!/bin/bash

# Function to detect the OS and execute commands accordingly
detect_os() {
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        OS=$ID
    else
        echo "OS not detected. Exiting..."
        exit 1
    fi
}

# Function to install on CentOS
install_centos() {
    echo "Installing Java 21, Python, Node.js, C++, Clang, Docker on CentOS..."

    # Install Java 21
    sudo yum install -y java-21-openjdk
    sudo yum install -y java-21-openjdk-devel

    # Install Python
    sudo yum install -y python3
    sudo yum install -y python3-pip
    sudo pip3 install numpy

    # Install Node.js
    curl -fsSL https://rpm.nodesource.com/setup_21.x | sudo bash -
    sudo yum install -y nodejs

    # Install C++ build tools
    sudo yum groupinstall -y "Development Tools"

    # Install Clang
    sudo yum install -y clang

    # Install Docker
    sudo yum install -y yum-utils
    sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
    sudo yum install -y docker-ce docker-ce-cli containerd.io
    sudo systemctl start docker
    sudo systemctl enable docker
}

# Function to install on Ubuntu
install_ubuntu() {
    echo "Installing Java 21, Python, Node.js, C++, Clang, Docker on Ubuntu..."

    # Install Java 21
    sudo apt update
    sudo apt install -y openjdk-21-jdk
    sudo apt install -y java-21-openjdk-devel

    # Install Python
    sudo apt install -y python3 python3-pip
    sudo pip3 install numpy

    # Install Node.js
    curl -fsSL https://deb.nodesource.com/setup_21.x | sudo -E bash -
    sudo apt install -y nodejs

    # Install C++ build tools
    sudo apt install -y build-essential

    # Install Clang
    sudo apt install -y clang

    # Install Docker
    sudo apt install -y apt-transport-https ca-certificates curl software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
    sudo apt update
    sudo apt install -y docker-ce docker-ce-cli containerd.io
    sudo systemctl start docker
    sudo systemctl enable docker
}

# Detect the OS and install the required packages
detect_os

if [ "$OS" == "centos" ]; then
    install_centos
elif [ "$OS" == "ubuntu" ]; then
    install_ubuntu
else
    echo "Unsupported OS. Exiting..."
    exit 1
fi

echo "Installation complete."