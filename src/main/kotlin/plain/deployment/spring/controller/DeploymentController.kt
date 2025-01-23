package plain.deployment.spring.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class DeploymentController {

    @Value("\${deployment}")
    lateinit var deployment: String

    @GetMapping
    fun getCurrentDeployment(): String {
        return deployment
    }

}