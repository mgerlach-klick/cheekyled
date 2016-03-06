(ns cheekyled.purehidapi
  (:import [purejavahidapi PureJavaHidApi HidDevice HidDeviceInfo]))



(defn write
  ([device message report-id]
   (write device message 64 report-id))
  ([device message packet-length report-id]
   (.setOutputReport device report-id message packet-length (byte report-id))))


(defn find-led []
  (let [vendor 0x1d34
        device 0x0013]
    (first
     (filter (fn [d] (and
                     (= (.getVendorId d) vendor)
                     (= (.getProductId d) device)))
             (PureJavaHidApi/enumerateDevices)))))

(defn with-led [f]
  (let [led (-> (find-led)
                .getPath
                (PureJavaHidApi/openDevice))]
    (f led)
    (.close led)))

(defn write-diamond [led]
  (let [diamond [[0x00, 0x00, 0xFF,0xFE,0xFF, 0xFF,0xFD,0x7F,],
                 [0x00, 0x02, 0xFF,0xFB,0xBF, 0xFF,0xF7,0xDF,],
                 [0x00, 0x04, 0xFF,0xFB,0xBF, 0xFF,0xFD,0x7F,],
                 [0x00, 0x06, 0xFF,0xFE,0xFF,]]]
    (doseq [row diamond]
      (write led (byte-array row) 0))))
