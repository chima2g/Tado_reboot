/**
 *  Reboot For Internet Connection
 *
 *  Copyright 2020 David Chima
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Reboot For Internet Connection",
    namespace: "chima2g",
    author: "David Chima",
    description: "When Tado looses connection to the internet it does not re-establish connection regardless of whether it has a working connection until it is turned off and on. This turns a smart switch off for 1 minute every hour to do so.",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Tado bridge switch") {
        input "tadoBridge", "capability.switch", required: true
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    subscribe(tadoBridge, "switch.off", bridgeOffHandler)
    subscribe(tadoBridge, "switch.on", bridgeOnHandler)
    tadoBridge.off()
    runIn(1*60, turnOnBridge)
}

def bridgeOffHandler(evt) {
	log.debug "bridge off event"
	runIn(1*60, turnOnBridge)
}

def bridgeOnHandler(evt) {
	log.debug "bridge on event"
	runIn(59*60, turnOffBridge)
}

def turnOnBridge() {
	log.debug "bridge on"
	tadoBridge.on()
}

def turnOffBridge() {
	log.debug "bridge off"
	tadoBridge.off()
}
