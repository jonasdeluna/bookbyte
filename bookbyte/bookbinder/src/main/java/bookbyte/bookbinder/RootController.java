package bookbyte.bookbinder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin
@Controller
public class RootController {

    @RequestMapping("/")
    public RedirectView indexRedirect() {
        return new RedirectView("/docs/docs.html");
    }

}
