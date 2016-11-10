package main

import "log"
import "fmt"
import "errors"
import "flag"
import "os"
import "io/ioutil"
import "strconv"
import "image"
import "image/color"
import "image/png"
import "strings"
import "math"

var problem_file = flag.String("problem", "", "the file containing the tsp problem")
var answer_file = flag.String("answer", "", "the file containing the tsp path")
var outfile = flag.String("output_file", "output.png", "png output file")
var width = flag.Int("width", 1024, "width of img")
var height = flag.Int("height", 1024, "height of img")

type Node struct {
	index int
	x, y float64
}

func dist(a, b Node) int {
	return int(math.Floor(math.Sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)) + 0.5))
}

func Usage() {
	fmt.Fprintf(os.Stderr, "usage: %v [-r] [path ...]\n", os.Args[0])
    flag.PrintDefaults()
    os.Exit(2)
}

func init() {
	flag.Usage = Usage
}

func read_answer(filename string) (path []int) {
	f, err := os.Open(filename)
	if err != nil { log.Fatal(err) }

	buf, err2 := ioutil.ReadAll(f)
	if err2 != nil { log.Fatal(err2) }
	
	str := string(buf)
	str = strings.Replace(str, "\n", " ", -1)
	str = strings.Replace(str, "\t", " ", -1)
	items := strings.Split(str, " ")
	for _, item := range items {
		if len(item) > 0 {
			val, err := strconv.Atoi(item)
			if err == nil {
				path = append(path, val)
			}
		}
	}
	return
}

func read_tsp(filename string) (nodes []Node) {
	f, err := os.Open(filename)
	if err != nil { log.Fatal(err) }

	buf, err2 := ioutil.ReadAll(f)
	if err2 != nil { log.Fatal(err2) }
	
	str := string(buf)
	lines := strings.Split(str, "\n")
	for i, line := range lines {
		if len(line) == 0 { continue }
		if strings.Contains(line, ":") { continue }
		if strings.Contains(line, "NODE_COORD_SECTION") { continue }
		if strings.Contains(line, "EOF") { continue }
		vals := strings.Split(line, " ")
		if len(vals) != 3 { log.Fatal(errors.New(fmt.Sprintf("error in file, line %v", i))) }
		index, err_1 := strconv.Atoi(vals[0])
		x, err_2 := strconv.ParseFloat(vals[1], 64)
		y, err_3 := strconv.ParseFloat(vals[2], 64)
		if err_1 != nil || err_2 != nil || err_3 != nil { log.Fatal(errors.New(fmt.Sprintf("error in file, line %v", i))) }
		nodes = append(nodes, Node{index, x, y})
	}
	return
}

func fill(img *image.RGBA) {
	for x := 0; x < img.Bounds().Dx(); x++ {
		for y := 0; y < img.Bounds().Dy(); y++ {
			img.Set(x, y, color.White)
		}
	}
}

func draw_square(rgba *image.RGBA, x, y, width float64) {
	for iy := int(y - width); iy < int(y + float64(width)); iy++ {
		for ix := int(x - width); ix < int(x + float64(width)); ix++ {
			rgba.Set(ix, iy, color.Red)
		}
	}
}

func xor(a, b bool) bool {
	return (a && !b) || (!a && b)
}

func draw_line(rgba *image.RGBA, x, y, x2, y2 float64) {
	//x_less, y_less := x < x2, y < y2
	y_less := y < y2
	dx, dy := x2 - x, y2 - y
	length := math.Sqrt(math.Pow(dx, 2) + math.Pow(dy, 2))
	ddx, ddy := dx / length, dy / length

	ix := x
	for iy := y; !xor(iy < y2, y_less); iy += ddy {
		ix += ddx
		//for ix := x; !xor(ix < x2, x_less); ix += ddx {
			//fmt.Println(ix, iy, ddx, ddy, length, dx, dy)
			//os.Exit(1)
		rgba.Set(int(ix), int(iy), color.RGBA{255, 0, 0, 255})
		//}
	}

}

func calculate_distance(nodes []Node, path []int) {
	node_map := make(map[int]Node, 0)
	visited := make(map[int]bool)

	for _, node := range nodes {
		node_map[node.index] = node
	}

	length := 0
	for i := 0; i < len(path) - 1; i++ {
		n1, n2 := node_map[path[i]], node_map[path[i + 1]]
		length += dist(n1, n2)
		visited[n1.index] = true
		visited[n2.index] = true
	}
	length += dist(node_map[path[len(path)-1]], node_map[path[0]])
	
	for _, node := range nodes {
		if _, ok := visited[node.index]; !ok {
			log.Fatal(errors.New("not all nodes visited"))
		}
	}

	fmt.Println("length: ", length)
}

func draw(outfile string, nodes []Node, path []int) {
	node_map := make(map[int]Node, 0)
	var max_x, max_y float64 = 0, 0

	for _, node := range nodes {
		node_map[node.index] = node
		max_x = math.Max(max_x, node.x)
		max_y = math.Max(max_y, node.y)
	}

	f, err := os.Create(outfile)
	if err != nil { log.Fatal(err) }
	img := image.NewRGBA(image.Rect(0, 0, *width, *height))
	
	fill(img)

	for _, node := range nodes {
		draw_x, draw_y := node.x / max_x * float64(*width), node.y / max_y * float64(*height)
		draw_square(img, draw_x, draw_y, 3)
	}

	for i := range path {
		
		index, index_next := path[i], path[(i+1) % len(path)]

		node := node_map[index]
		node_next := node_map[index_next]
		draw_x, draw_y := node.x / max_x * float64(*width), node.y / max_y * float64(*height)
		draw_x_next, draw_y_next := node_next.x / max_x * float64(*width), node_next.y / max_y * float64(*height)
		draw_line(img, draw_x, draw_y, draw_x_next, draw_y_next)
	}

	//output
	err_encode := png.Encode(f, img)
	if err_encode != nil { log.Fatal(err_encode) }
}

func main() {
	flag.Parse()
	if len(*problem_file) == 0 || len(*answer_file) == 0 { Usage() }
	nodes := read_tsp(*problem_file)
	path := read_answer(*answer_file)
	draw(*outfile, nodes, path)
	calculate_distance(nodes, path)
	fmt.Println("written to ", *outfile)
}
