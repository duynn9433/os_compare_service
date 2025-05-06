FROM httpd:2.4

# Enable required modules
RUN sed -i '/mod_proxy.so/s/^#//g' /usr/local/apache2/conf/httpd.conf && \
    sed -i '/mod_proxy_balancer.so/s/^#//g' /usr/local/apache2/conf/httpd.conf && \
    sed -i '/mod_proxy_http.so/s/^#//g' /usr/local/apache2/conf/httpd.conf && \
    sed -i '/mod_slotmem_shm.so/s/^#//g' /usr/local/apache2/conf/httpd.conf && \
    sed -i '/mod_lbmethod_byrequests.so/s/^#//g' /usr/local/apache2/conf/httpd.conf

# Add proxy config
COPY proxy.conf /usr/local/apache2/conf/extra/proxy.conf
RUN echo "Include conf/extra/proxy.conf" >> /usr/local/apache2/conf/httpd.conf