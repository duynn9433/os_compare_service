FROM httpd:2.4

# Bật các module proxy + MPM event
RUN apt-get update && apt-get install -y --no-install-recommends apache2-utils \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN sed -i 's/#LoadModule slotmem_shm_module/LoadModule slotmem_shm_module/' /usr/local/apache2/conf/httpd.conf \
 && echo "LoadModule proxy_module modules/mod_proxy.so" >> /usr/local/apache2/conf/httpd.conf \
 && echo "LoadModule proxy_http_module modules/mod_proxy_http.so" >> /usr/local/apache2/conf/httpd.conf \
 && echo "LoadModule proxy_balancer_module modules/mod_proxy_balancer.so" >> /usr/local/apache2/conf/httpd.conf \
 && echo "LoadModule lbmethod_byrequests_module modules/mod_lbmethod_byrequests.so" >> /usr/local/apache2/conf/httpd.conf \
 && echo "Include conf/extra/proxy.conf" >> /usr/local/apache2/conf/httpd.conf \
 && echo "Include conf/extra/mpm.conf" >> /usr/local/apache2/conf/httpd.conf

COPY proxy.conf /usr/local/apache2/conf/extra/proxy.conf
COPY mpm.conf /usr/local/apache2/conf/extra/mpm.conf