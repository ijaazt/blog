import React, {Component} from 'react';
import Article from './Article';
import Preview from './Preview';
import Blog from "./Blog";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: [],
        }
    }

    getArticles = () => {
        fetch("http://localhost:8080/api/article/")
            .then(function (res) {
                return res.json()
            })
            .then(res => {
                this.setState(prevState => ({
                    ...prevState,
                    articles: res.content
                }))
            })
    };
    componentWillMount() {
        this.getArticles();
    }

    handleViewing(id) {
        this.setState(prevState => ({
            ...prevState,
            isViewing: true,
            viewId: id
        }))
    }
    goBack() {
        this.setState(prevState => ({
            ...prevState,
            isViewing: false,
        }))
    }

    getSelectedArticle() {
        let article
        this.state.articles.forEach(content => {
            if (content.id === this.state.viewId)
                article = <Article article={content} onClick={() => {
                    this.goBack()
                }}/>
        })
        return article
    }
    render() {
        let preview = (!this.state.isViewing) ?
            <Blog title={this.props.title} articles={this.state.articles} onClick={id => { this.handleViewing(id) }}/>
            : this.getSelectedArticle()
        return (
            <div className="App">
                {preview}
            </div>
        );
    }
}

export default App;
