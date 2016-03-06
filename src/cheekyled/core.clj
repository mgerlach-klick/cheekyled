(ns cheekyled.core
  (:gen-class)
  (:import [org.hid4java HidDevice HidDeviceManager HidManager HidServices HidServicesListener]
           [org.hid4java.event HidServicesEvent]
           [org.hid4java.jna HidApi]))


(defn write
  ;;generic
  ([device message packet-length report-id]
   (.write device message packet-length (byte report-id)))
  ;;specific to led
  ([device message]
   (write device (if (coll? message) (byte-array message) message ) 8 0)))


(defn find-led []
  (let [vendor 0x1d34
        device 0x0013]
   (HidDevice. (HidApi/enumerateDevices vendor device))))

(defn with-led [f]
  (let [led (find-led)]
    (.open led)
    (f led)
    (.close led)))

(defn write-diamond [led]
  (let [diamond [[0x00 0x00 0xFF 0xFE 0xFF 0xFF 0xFD 0x7F ] 
                 [0x00 0x02 0xFF 0xFB 0xBF 0xFF 0xF7 0xDF ] 
                 [0x00 0x04 0xFF 0xFB 0xBF 0xFF 0xFD 0x7F ] 
                 [0x00 0x06 0xFF 0xFE 0xFF]
                 ]]
    (dotimes [i 10]
      (doseq [row diamond]
        (write led row))
      (Thread/sleep 100))))
