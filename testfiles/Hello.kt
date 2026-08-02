import org.congocc.templates.Template
import org.congocc.templates.Extension

import java.util.function.Function
import java.util.Locale



fun main() {
    val text:String = "message";
    var data = HashMap<String,Any>()
    var data = HashMap<String,Any>()
    data.put("message", "Hello, World!")
/*
    val templateText = $$"""
    /* fuck a duck */
      Here is the $${text}: ${message}
      ${func(message)}
      ${message.Func}
      ${3.7.double.toString().Func}
      #var list = "java.util.ArrayList".new()
      #exec list.add(1)
      #exec list.add("2")
      #exec list.add([])
      ${list.size()}
      ${list.Size()}
      ${list.Size}
      ${message.toUpperCase(::Locale)}
      ${::Locale}
      ${list[0]::class::SimpleName}
      ${list[1]::class}
      ${list[2]::class}
    """.trim();
*/
    var data = HashMap<String,Any>()
    data.put("message", "Hello, World!");
    var func = Function {s:String -> s.lowercase()};
    Extension.register("Func", func);
    var template = Template(templateText);
    template.setLocale(Locale.FRANCE);
    data.put("func", func);
    var output = template.process(data);
    println(output)
}