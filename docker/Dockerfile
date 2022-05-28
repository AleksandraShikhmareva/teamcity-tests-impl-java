# Default arguments
ARG containerdIoLinuxComponentVersion='1.4.12-1'
ARG dockerComposeLinuxComponentVersion='1.28.5'
ARG dockerLinuxComponentVersion='5:20.10.12~3-0~ubuntu'
ARG dotnetLibs='libc6 libgcc1 libgssapi-krb5-2 libicu66 libssl1.1 libstdc++6 zlib1g'
ARG dotnetLinuxComponent='https://dotnetcli.blob.core.windows.net/dotnet/Sdk/6.0.100/dotnet-sdk-6.0.100-linux-x64.tar.gz'
ARG dotnetLinuxComponent_31='https://dotnetcli.azureedge.net/dotnet/Runtime/3.1.21/dotnet-runtime-3.1.21-linux-x64.tar.gz'
ARG dotnetLinuxComponent_50='https://dotnetcli.azureedge.net/dotnet/Runtime/5.0.12/dotnet-runtime-5.0.12-linux-x64.tar.gz'
ARG dotnetLinuxComponentSHA512='cb0d174a79d6294c302261b645dba6a479da8f7cf6c1fe15ae6998bc09c5e0baec810822f9e0104e84b0efd51fdc0333306cb2a0a6fcdbaf515a8ad8cf1af25b'
ARG dotnetLinuxComponentSHA512_31='cc4b2fef46e94df88bf0fc11cb15439e79bd48da524561dffde80d3cd6db218133468ad2f6785803cf0c13f000d95ff71eb258cec76dd8eb809676ec1cb38fac'
ARG dotnetLinuxComponentSHA512_50='32b5f86db3b1d4c21e3cf616d22f0e4a7374385dac0cf03cdebf3520dcf846460d9677ec1829a180920740a0237d64f6eaa2421d036a67f4fe9fb15d4f6b1db9'
ARG gitLFSLinuxComponentVersion='2.9.2-1'
ARG gitLinuxComponentVersion='1:2.36.1-0ppa1~ubuntu20.04.1'
ARG p4Version='2021.2-2273812'
ARG repo='https://hub.docker.com/r/jetbrains/'
ARG teamcityMinimalAgentImage='teamcity-minimal-agent:local-linux'

# The list of required arguments
# ARG dotnetLinuxComponent
# ARG dotnetLinuxComponentSHA512
# ARG dotnetLinuxComponent_31
# ARG dotnetLinuxComponentSHA512_31
# ARG dotnetLinuxComponent_50
# ARG dotnetLinuxComponentSHA512_50
# ARG teamcityMinimalAgentImage
# ARG dotnetLibs
# ARG gitLinuxComponentVersion
# ARG gitLFSLinuxComponentVersion
# ARG dockerComposeLinuxComponentVersion
# ARG dockerLinuxComponentVersion

# Id teamcity-agent
# Platform ${linuxPlatform}
# Tag ${versionTag}-linux${linuxVersion}
# Tag ${latestTag}
# Tag ${versionTag}
# Repo ${repo}
# Weight 1

## ${agentCommentHeader}

# Based on ${teamcityMinimalAgentImage}
FROM jetbrains/teamcity-minimal-agent:latest

USER root

COPY run-docker.sh /services/run-docker.sh

ARG dotnetCoreLinuxComponentVersion

    # Opt out of the telemetry feature
ENV DOTNET_CLI_TELEMETRY_OPTOUT=true \
    # Disable first time experience
    DOTNET_SKIP_FIRST_TIME_EXPERIENCE=true \
    # Configure Kestrel web server to bind to port 80 when present
    ASPNETCORE_URLS=http://+:80 \
    # Enable detection of running in a container
    DOTNET_RUNNING_IN_CONTAINER=true \
    # Enable correct mode for dotnet watch (only mode supported in a container)
    DOTNET_USE_POLLING_FILE_WATCHER=true \
    # Skip extraction of XML docs - generally not useful within an image/container - helps perfomance
    NUGET_XMLDOC_MODE=skip \
    GIT_SSH_VARIANT=ssh \
    DOTNET_SDK_VERSION=${dotnetCoreLinuxComponentVersion}

ARG dotnetLinuxComponent
ARG dotnetLinuxComponentSHA512
ARG dotnetLinuxComponent_31
ARG dotnetLinuxComponentSHA512_31
ARG dotnetLinuxComponent_50
ARG dotnetLinuxComponentSHA512_50
ARG dotnetLibs
ARG gitLinuxComponentVersion
ARG gitLFSLinuxComponentVersion
ARG dockerComposeLinuxComponentVersion
ARG dockerLinuxComponentVersion
ARG containerdIoLinuxComponentVersion
ARG p4Version

RUN apt-get update && \
# Install ${gitLinuxComponentName}
# Install ${gitLFSLinuxComponentName}
# Install Mercurial
    apt-get install -y mercurial apt-transport-https software-properties-common && \
    add-apt-repository ppa:git-core/ppa -y && \
    apt-get install -y git=${gitLinuxComponentVersion} git-lfs=${gitLFSLinuxComponentVersion} && \
    git lfs install --system && \
    # https://github.com/goodwithtech/dockle/blob/master/CHECKPOINT.md#dkl-di-0005
    apt-get clean && rm -rf /var/lib/apt/lists/* && \
# Install ${p4Name}
    apt-key adv --fetch-keys https://package.perforce.com/perforce.pubkey && \
    (. /etc/os-release && \
      echo "deb http://package.perforce.com/apt/$ID $VERSION_CODENAME release" > \
      /etc/apt/sources.list.d/perforce.list ) && \
    apt-get update && \
    (. /etc/os-release && apt-get install -y helix-cli-base="${p4Version}~$VERSION_CODENAME" helix-cli="${p4Version}~$VERSION_CODENAME" ) && \
    # https://github.com/goodwithtech/dockle/blob/master/CHECKPOINT.md#dkl-di-0005
    apt-get clean && rm -rf /var/lib/apt/lists/* && \
# Install ${dockerLinuxComponentName}, ${containerdIoLinuxComponentName}
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" && \
    apt-cache policy docker-ce && \
    apt-get update && \
    apt-get install -y  docker-ce=${dockerLinuxComponentVersion}-$(lsb_release -cs) \
                        docker-ce-cli=${dockerLinuxComponentVersion}-$(lsb_release -cs) \
                        containerd.io:amd64=${containerdIoLinuxComponentVersion} \
                        systemd && \
    systemctl disable docker && \
    sed -i -e 's/\r$//' /services/run-docker.sh && \
# Install chorome dependencies
    apt-get install -y  xvfb \
                        libxi6 \
                        libgconf-2-4 \
                        wget && \
# Install choromebrowser
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt install -y ./google-chrome-stable_current_amd64.deb && \
# Install choromedriver
    wget https://chromedriver.storage.googleapis.com/102.0.5005.61/chromedriver_linux64.zip && \
    unzip chromedriver_linux64.zip && \
    mv chromedriver /usr/bin/chromedriver && \
    chown root:root /usr/bin/chromedriver && \
    chmod +x /usr/bin/chromedriver && \
# Install allure-commandline
    curl -o allure-2.13.8.tgz -OLs https://repo.maven.apache.org/maven2/io/qameta/allure/allure-commandline/2.13.8/allure-commandline-2.13.8.tgz && \
    tar -zxvf allure-2.13.8.tgz -C /opt/ && \
    ln -s /opt/allure-2.13.8/bin/allure /usr/bin/allure && \
# Install [Docker Compose v.${dockerComposeLinuxComponentVersion}](https://github.com/docker/compose/releases/tag/${dockerComposeLinuxComponentVersion})
    curl -SL "https://github.com/docker/compose/releases/download/${dockerComposeLinuxComponentVersion}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose && \
# Dotnet
    apt-get install -y --no-install-recommends ${dotnetLibs} && \
    # https://github.com/goodwithtech/dockle/blob/master/CHECKPOINT.md#dkl-di-0005
    apt-get clean && rm -rf /var/lib/apt/lists/* && \
    mkdir -p /usr/share/dotnet && \
# Install [${dotnetLinuxComponentName_31}](${dotnetLinuxComponent_31})
    curl -SL ${dotnetLinuxComponent_31} --output /tmp/dotnet.tar.gz && \
    echo "${dotnetLinuxComponentSHA512_31} */tmp/dotnet.tar.gz" | sha512sum -c -; \
    tar -zxf /tmp/dotnet.tar.gz -C /usr/share/dotnet && \
    rm /tmp/dotnet.tar.gz && \
    find /usr/share/dotnet -name "*.lzma" -type f -delete && \
# Install [${dotnetLinuxComponentName_50}](${dotnetLinuxComponent_50})
    curl -SL ${dotnetLinuxComponent_50} --output /tmp/dotnet.tar.gz && \
    echo "${dotnetLinuxComponentSHA512_50} */tmp/dotnet.tar.gz" | sha512sum -c -; \
    tar -zxf /tmp/dotnet.tar.gz -C /usr/share/dotnet && \
    rm /tmp/dotnet.tar.gz && \
    find /usr/share/dotnet -name "*.lzma" -type f -delete && \
# Install [${dotnetLinuxComponentName}](${dotnetLinuxComponent})
    curl -SL ${dotnetLinuxComponent} --output /tmp/dotnet.tar.gz && \
    echo "${dotnetLinuxComponentSHA512} */tmp/dotnet.tar.gz" | sha512sum -c -; \
    tar -zxf /tmp/dotnet.tar.gz -C /usr/share/dotnet && \
    rm /tmp/dotnet.tar.gz && \
    find /usr/share/dotnet -name "*.lzma" -type f -delete && \
    ln -s /usr/share/dotnet/dotnet /usr/bin/dotnet && \
# Trigger .NET CLI first run experience by running arbitrary cmd to populate local package cache
    dotnet help && \
    dotnet --info && \
# Other
    apt-get clean && rm -rf /var/lib/apt/lists/* && \
    chown -R buildagent:buildagent /services && \
    usermod -aG docker buildagent

# A better fix for TW-52939 Dockerfile build fails because of aufs
VOLUME /var/lib/docker

USER buildagent